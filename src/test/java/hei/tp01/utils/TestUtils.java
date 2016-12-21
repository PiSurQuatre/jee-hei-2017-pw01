package hei.tp01.utils;

import hei.tp01.controller.ClientsServlet;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.*;

public class TestUtils {

    private TestUtils(){
        super();
    }

    public static void shouldHaveMethod(Class<?> clazz,String methodName, Class<?>... parametersType) throws NoSuchMethodException {
        //GIVEN
        //WHEN
        Method initMethod = clazz.getDeclaredMethod(methodName, parametersType);
        //THEN
        assertThat(initMethod).isNotNull();
    }
}
