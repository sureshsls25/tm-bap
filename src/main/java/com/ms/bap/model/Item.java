package com.ms.bap.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ms.common.model.common.Skill;

import java.util.List;

public class Item extends com.ms.common.model.common.Item {
    @JsonProperty("skills")
    private List<Skill> skills;

    public List<Skill> getSkills() {
        return skills;
    }

    public void setSkills(List<Skill> skills) {
        this.skills = skills;
    }
}
