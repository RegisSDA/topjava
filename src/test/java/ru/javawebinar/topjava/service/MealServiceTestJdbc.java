package ru.javawebinar.topjava.service;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;

/**
 * Created by MSI on 15.08.2017.
 */

@ActiveProfiles(profiles = Profiles.JPA)
public class MealServiceTestJdbc extends MealServiceTest {
}
