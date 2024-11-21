package com.example.demo.actors;

public enum DestructionType {
    COLLISION,           // When enemy collides with player plane
    PROJECTILE_KILL,    // When enemy is destroyed by player projectile
    PENETRATED_DEFENSE  // When enemy passes defense line
}
