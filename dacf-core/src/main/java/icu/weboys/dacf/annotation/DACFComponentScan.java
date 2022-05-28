package icu.weboys.dacf.annotation;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DACFComponentScan {
    String[] basePackages();
}
