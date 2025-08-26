package com.araculture.controllers;

import com.araculture.models.Profile;
import com.araculture.repositories.ProfileRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profiles")
public class ProfileController {

    private final ProfileRepository profileRepository;

    public ProfileController(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @GetMapping
    public List<Profile> getAllProfiles() {
        return profileRepository.findAll();
    }

    @GetMapping("/{id}")
    public Profile getProfile(@PathVariable Long id) {
        return profileRepository.findById(id).orElse(null);
    }

    @PostMapping
    public Profile createProfile(@RequestBody Profile profile) {
        return profileRepository.save(profile);
    }

    @PutMapping("/{id}")
    public Profile updateProfile(@PathVariable Long id, @RequestBody Profile updatedProfile) {
        return profileRepository.findById(id)
                .map(profile -> {
                    profile.setBio(updatedProfile.getBio());
                    profile.setPhone(updatedProfile.getPhone());
                    profile.setAddress(updatedProfile.getAddress());
                    return profileRepository.save(profile);
                })
                .orElse(null);
    }

    @DeleteMapping("/{id}")
    public void deleteProfile(@PathVariable Long id) {
        profileRepository.deleteById(id);
    }
}
