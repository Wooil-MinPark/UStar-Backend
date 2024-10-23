package com.wooil.ustar.service;

import com.wooil.ustar.domain.Star;
import com.wooil.ustar.repository.StarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.List;

@Service
public class StarService {

    final private StarRepository starRepository;

    @Autowired
    public StarService(StarRepository starRepository) {this.starRepository = starRepository;}

    public void getStar() throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        Star star = starRepository.findByStarName("sirius");

        Class<?> clazz = Class.forName("com.wooil.ustar.domain.Star");
        Field field = clazz.getDeclaredField("starName");
        field.setAccessible(true);
        Object value = field.get(star);
        field.set(value, "newStarName");

        String a= "1";
    }
}
