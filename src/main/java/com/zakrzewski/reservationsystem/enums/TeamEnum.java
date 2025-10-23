package com.zakrzewski.reservationsystem.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Getter
public enum TeamEnum {
    DATABASE("database"),
    ACCOUNT_MANAGER("accountManager"),
    OMEGA("omega"),
    ALPHA("alpha"),
    ADMIN("admin"),
    TESTER("tester"),
    DIRECTOR("director");

    private static final Map<String, TeamEnum> TEAM_MAP = new HashMap<>();
    static {
        for (TeamEnum team : values()) {
            TEAM_MAP.put(team.teamName.toLowerCase(), team);
        }
    }

    private final String teamName;

    TeamEnum(final String teamName) {
        this.teamName = teamName;
    }

    public static Optional<TeamEnum> fromTeamName(final String teamName) {
        if (teamName == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(TEAM_MAP.get(teamName.toLowerCase()));
    }
}
