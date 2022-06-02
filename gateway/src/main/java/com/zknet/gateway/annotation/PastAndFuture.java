package com.zknet.gateway.annotation;

import cn.hutool.core.date.DateUtil;
import com.zknet.gateway.exception.BizCodeEnum;
import com.zknet.gateway.exception.BizException;
import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import javax.validation.constraints.NotNull;
import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(value = PastAndFuture.List.class)
@Documented
@Constraint(
        validatedBy = {PastAndFuture.IntervalValidator.class}
)
public @interface PastAndFuture  {
    String message() default "TIMESTAMP_EXPIRATION";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    public @interface List {
        PastAndFuture[] value();
    }

    long pastSecond() default 1 * 60L;


    long futureSecond() default 1 * 60L;

    BizCodeEnum bizCodeEnum();

    class IntervalValidator implements ConstraintValidator<PastAndFuture, Integer> {
        private PastAndFuture pastAndFuture;

        @Override
        public void initialize(PastAndFuture pastAndFuture) {
            this.pastAndFuture = pastAndFuture;
        }

        @Override
        public boolean isValid(Integer param, ConstraintValidatorContext context) {
            Long currentSeconds = DateUtil.currentSeconds();
            Long pastSecond = currentSeconds - pastAndFuture.pastSecond();
            Long futureSecond = currentSeconds + pastAndFuture.futureSecond();
            if (pastSecond.intValue() < param && param < futureSecond.intValue()) {
                return true;
            }
            throw new BizException(pastAndFuture.bizCodeEnum());
        }
    }
}