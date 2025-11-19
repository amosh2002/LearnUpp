package com.learnupp.data.profile

import com.learnupp.domain.model.Profile

class MockProfileApi : ProfileApi {
    override suspend fun fetchProfile(): Profile {
        return Profile(
            id = "lecturer-1",
            fullName = "Lecturer Name",
            title = "Digital Marketing Lead",
            avatarUrl = "https://images.unsplash.com/photo-1544723795-3fb6469f5b39?q=80&w=800&auto=format&fit=crop",
            isLecturer = true,
            students = 2847,
            courses = 12,
            rating = 4.8,
            followers = 20_400,
            following = 320,
            posts = 96,
            about = "With over 10 years of experience in digital marketing, strategic marketing campaigns and data-driven practices."
        )
    }
}

