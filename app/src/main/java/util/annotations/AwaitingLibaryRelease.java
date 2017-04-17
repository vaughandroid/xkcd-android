package util.annotations;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
public @interface AwaitingLibaryRelease {

    String library();
    String comment() default "";
}
