package com.example.demo.actors.planes;

import com.example.demo.actors.planes.bosses.Boss;
import com.example.demo.actors.planes.bosses.MonstrousNightmare;

public class PlaneFactory {

    //USER
    public static UserPlane createUserPlane() {
        return new UserPlane(
                "userplane.png",       // imageName
                55,                  // imageHeight
                5.0,                   // initialX
                300.0,                 // initialY
                5                      // initialHealth
        );
    }

    public static Toothless createToothless() {
        return new Toothless(
                "toothless.png",       // imageName
                200,                  // imageHeight
                5.0,                   // initialX
                300.0,                 // initialY
                5                      // initialHealth
        );
    }

    //ENEMY
    public static EnemyPlane createEnemyPlane(double initialX, double initialY) {
        return new EnemyPlane(
                "enemyplane.png",       // imageName
                80,               // imageHeight
                initialX,                   // initialX
                initialY,                 // initialY
                1                      // initialHealth
        );
    }

    public static Seashocker createSeashocker(double initialX, double initialY) {
        return new Seashocker(
                "seashocker.png",       // imageName
                100,               // imageHeight
                initialX,                   // initialX
                initialY,                 // initialY
                1                      // initialHealth
        );
    }

    //BOSS
    public static Boss createBoss() {
        return new Boss(
                "bossplane.png",       // imageName
                100,                  // imageHeight
                800.0,                   // initialX
                400.0,                 // initialY
                1                      // initialHealth
        );
    }

    public static MonstrousNightmare createMonstrousNightmare() {
        return new MonstrousNightmare(
                "monstrousnightmare.png",       // imageName
                200,                  // imageHeight
                1000.0,                   // initialX
                400.0,                 // initialY
                1                      // initialHealth
        );
    }
}
