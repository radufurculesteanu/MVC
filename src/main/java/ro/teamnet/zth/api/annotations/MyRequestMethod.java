package ro.teamnet.zth.api.annotations;

import java.lang.annotation.*;

/**
 * Created by Radu.Furculesteanu on 7/20/2017.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyRequestMethod {
    String urlPath();
    String methodType();
}
