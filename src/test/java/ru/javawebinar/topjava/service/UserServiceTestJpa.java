package ru.javawebinar.topjava.service;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;

/**
 * Created by MSI on 16.08.2017.
 */

@ActiveProfiles(profiles = Profiles.JPA)
public class UserServiceTestJpa extends UserServiceTest {
}
