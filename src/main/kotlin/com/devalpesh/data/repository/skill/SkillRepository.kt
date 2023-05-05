package com.devalpesh.data.repository.skill

import com.devalpesh.data.models.Skill

interface SkillRepository {

    suspend fun getSkills() : List<Skill>
}