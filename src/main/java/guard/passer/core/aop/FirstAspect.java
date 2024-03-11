package guard.passer.core.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Aspect
@Component
public class FirstAspect {

    @Pointcut("@within(org.springframework.stereotype.Component)")
    public void isControllerLayer() {
    }

    @Pointcut("within(guard.passer.core.service.*Service)")
    public void isServiceLayer() {
    }

    //    @Pointcut("target(org.springframework.data.repository.Repository)")
    @Pointcut("this(org.springframework.data.repository.Repository)")
    public void isRepositoryLayer() {
    }

    @Pointcut("isControllerLayer() && @annotation(org.springframework.web.bind.annotation.GetMapping)")
    public void hasGetMapping() {
    }

    @Pointcut("isControllerLayer() && args(org.springframework.ui.Model,..)")
    public void hasModelParamFirst() {
    }

    @Pointcut("execution(public * guard.passer.core.service.*Service.findById(*))")
    public void anyFindByIdServiceMethod() {
    }

    @Before("anyFindByIdServiceMethod() " +
            "&& args(id) " +
            "&& target(service) " +
            "&& this(serviceProxy) " +
            "&& @within(transactional)")
    public void addLogingBefore(JoinPoint joinPoint,
                                Object id,
                                Object service,
                                Object serviceProxy,
                                Transactional transactional) {
        log.info("@Before: invoke find by id method in class {}, with id {}", service, id);
    }

    @AfterReturning(value = "anyFindByIdServiceMethod() " +
            "&& target(service)",
            returning = "result")
    public void addLogingAfterReturning(Object result,
                                        Object service) {
        log.info("@AfterReturning: invoke find by id method in class {}, result {}", service, result);
    }

    @AfterThrowing(value = "anyFindByIdServiceMethod() " +
            "&& target(service)",
            throwing = "ex")
    public void addLogingAfterThrowing(Throwable ex,
                                       Object service) {
        log.info("@AfterThrowing: invoke find by id method in class {}, exception {}: {}", service,
                ex.getClass(), ex.getMessage());
    }

    @After("anyFindByIdServiceMethod() && target(service)")
    public void addLogingAfterFinally(Object service) {
        log.info("@After: invoke find by id method in class {}", service);
    }

    @Around("anyFindByIdServiceMethod() " +
            "&& target(service) " +
            "&& args(id)")
    public Object addLoggingAround(ProceedingJoinPoint joinPoint,
                                   Object service,
                                   Object id) throws Throwable {
        log.info("@Around before - invoked findById method in class {}, with id {}", service, id);
        try {
            Object result = joinPoint.proceed();
            log.info("@Around after returning - invoked findById method in class {}, result {}", service, result);
            return result;
        } catch (Throwable ex) {
            log.info("@Around after throwing - invoked findById method in class {}, exception {}: {}", service, ex.getClass(), ex.getMessage());
            throw ex;
        } finally {
            log.info("@Around after (finally) - invoked findById method in class {}", service);
        }
    }
}
