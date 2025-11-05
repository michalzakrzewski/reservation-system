package com.zakrzewski.reservationsystem.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Getter
public enum TeamEnum {
    DATABASE,
    ACCOUNT_MANAGER,
    OMEGA,
    ALPHA,
    ADMIN,
    TESTER,
    PRODUCT,
    HUMAN_RESOURCES,
    DIRECTOR;

    private static final Map<String, TeamEnum> TEAM_MAP = new HashMap<>();

    static {
        for (TeamEnum team : values()) {
            TEAM_MAP.put(team.name(), team);
        }
    }

    TeamEnum() {
    }

    public static Optional<TeamEnum> fromTeamName(String teamName) {
        return Optional.ofNullable(TEAM_MAP.get(teamName));
    }
}
