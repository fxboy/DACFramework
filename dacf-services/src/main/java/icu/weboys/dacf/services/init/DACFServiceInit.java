package icu.weboys.dacf.services.init;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public class DACFServiceInit {
    @Getter
    @Setter
    @Value("${dacf.services.maximumPoolSize}")
    Integer maximumPoolSize = 60;
    public void init(ApplicationContext applicationContext){

    }

}
