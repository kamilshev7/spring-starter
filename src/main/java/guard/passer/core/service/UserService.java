package guard.passer.core.service;

import com.querydsl.core.types.Predicate;
import guard.passer.core.database.entity.User;
import guard.passer.core.database.querydsl.QPredicates;
import guard.passer.core.database.reporitory.UserRepository;
import guard.passer.core.dto.UserCreateEditDto;
import guard.passer.core.dto.UserFilter;
import guard.passer.core.dto.UserReadDto;
import guard.passer.core.mapper.UserCreateEditMapper;
import guard.passer.core.mapper.UserReadMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static guard.passer.core.database.entity.QUser.user;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserReadMapper userReadMapper;
    private final UserCreateEditMapper userCreateEditMapper;
    private final ImageService imageService;

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

    public Optional<UserReadDto> findById(Long id) {
        return userRepository.findById(id)
                .map(user -> userReadMapper.map(user));
    }

    public Optional<byte[]> findAvatar(Long id) {
        return userRepository.findById(id)
                .map(user -> user.getImage())
                .filter(image -> StringUtils.hasText(image))
                .flatMap(s -> imageService.get(s));
    }

    @Transactional
    public UserReadDto create(UserCreateEditDto userCreateEditDto) {
        return Optional.of(userCreateEditDto)
                .map(userDto -> {
                    uploadImage(userDto.getImage());
                    return userCreateEditMapper.map(userDto);
                })
                .map(user -> userRepository.save(user))
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
    private void uploadImage(MultipartFile image){
        if(!image.isEmpty()){
            imageService.upload(image.getOriginalFilename(), image.getInputStream());
        }
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
