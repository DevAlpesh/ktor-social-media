package com.devalpesh.service

import com.devalpesh.data.models.Skill
import com.devalpesh.data.repository.skill.SkillRepository

class SkillService(
    private val repository: SkillRepository
) {
    suspend fun getSkills() : List<Skill>{
        return repository.getSkills()
    }
}