package guard.passer.core.service;

import com.querydsl.core.types.Predicate;
import guard.passer.core.database.entity.Phone;
import guard.passer.core.database.entity.User;
import guard.passer.core.database.querydsl.QPredicates;
import guard.passer.core.database.reporitory.PhoneRepository;
import guard.passer.core.database.reporitory.UserRepository;
import guard.passer.core.dto.PhoneReadDto;
import guard.passer.core.dto.UserCreateEditDto;
import guard.passer.core.dto.UserFilter;
import guard.passer.core.dto.UserReadDto;
import guard.passer.core.mapper.PhoneReadMapper;
import guard.passer.core.mapper.UserCreateEditMapper;
import guard.passer.core.mapper.UserReadMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static guard.passer.core.database.entity.QUser.user;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@EnableCaching
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PhoneRepository phoneRepository;
    private final UserReadMapper userReadMapper;
    private final PhoneReadMapper phoneReadMapper;
    private final UserCreateEditMapper userCreateEditMapper;
    private final ImageService imageService;
    private final EntityManager entityManager;

    public Page<UserReadDto> findAll(UserFilter filter, Pageable pageable) {
        Predicate predicate = QPredicates.builder()
                .add(filter.firstname(), user.firstname::containsIgnoreCase)
                .add(filter.lastname(), user.lastname::containsIgnoreCase)
                .add(filter.birthDate(), user.birthDate::before)
                .build();
        return userRepository.findAll(predicate, pageable)
                .map(user1 -> userReadMapper.map(user1));
    }

    public List<UserReadDto> findAll() {
        return userRepository.findAll().stream()
                .map(user -> userReadMapper.map(user))
                .collect(Collectors.toList());
    }

    //    @Cacheable(value = "user", key = "#id")
    public Optional<UserReadDto> findById(Long id) {
//        entityManager.clear();
        return userRepository.findById(id)
                .map(user -> userReadMapper.map(user));
    }

    public Optional<byte[]> findAvatar(Long id) {
        return userRepository.findById(id)
                .map(user -> user.getImage())
                .filter(image -> StringUtils.hasText(image))
                .flatMap(s -> imageService.get(s));
    }

    //    @Cacheable("user")
    @Transactional
    public UserReadDto create(UserCreateEditDto userCreateEditDto) {
        return Optional.of(userCreateEditDto)
                .map(userDto -> {
                    uploadImage(userDto.getImage());
                    return userCreateEditMapper.map(userDto);
                })
                .map(user -> {
                    User savedUser = userRepository.saveAndFlush(user);
                    savedUser.getPhones().stream().forEach(phone -> {
                        phone.setOwner(savedUser);
                        phoneRepository.saveAndFlush(phone);
                    });
                    return savedUser;
                })
                .map(user -> userReadMapper.map(user))
                .orElseThrow();
    }

    @Transactional
    public Optional<UserReadDto> update(Long id, UserCreateEditDto userCreateEditDto) {
        return userRepository.findById(id)
                .map(user -> {
                    uploadImage(userCreateEditDto.getImage());
                    return userCreateEditMapper.map(userCreateEditDto, user);
                })
                .map(user -> userRepository.saveAndFlush(user))
                .map(user -> userReadMapper.map(user));
    }

    @SneakyThrows
    private void uploadImage(MultipartFile image) {
        if (image != null && !image.isEmpty()) {
            imageService.upload(image.getOriginalFilename(), image.getInputStream());
        }
    }

    @Transactional
//    @CacheEvict(value = "user", allEntries = true)
    @Modifying(clearAutomatically = true)
    public boolean deletePhoneById(Long id) {
        return phoneRepository.findById(id)
                .map(phone -> {
                    phoneRepository.deleteById(phone.getId());
                    phoneRepository.flush();
                    entityManager.clear();
                    return true;
                }).orElse(false);

    }

    @Transactional
    public boolean deleteAllPhonesByUser(Long id) {
        return userRepository.findById(id)
                .map(user -> {
                    phoneRepository.deleteAllByOwner(user);
                    phoneRepository.flush();
                    entityManager.clear();
                    return true;
                })
                .orElse(false);
    }

    public Optional<PhoneReadDto> findPhoneById(Long id) {
        return phoneRepository.findById(id)
                .map(phone -> phoneReadMapper.map(phone));
    }

    @Transactional
    public boolean delete(Long id) {
        return userRepository.findById(id)
                .map(user -> {
                    userRepository.delete(user);
                    userRepository.flush();
                    return true;
                }).orElse(false);

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .map(user -> new org.springframework.security.core.userdetails.User(
                        user.getUsername(),
                        user.getPassword(),
                        Collections.singleton(user.getRole())
                )).orElseThrow(() -> new UsernameNotFoundException("Failed to find user:" + username));
    }
}
