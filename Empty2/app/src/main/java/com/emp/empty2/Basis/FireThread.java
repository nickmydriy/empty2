package com.emp.empty2.Basis;

import com.emp.empty2.GlobalValues;
import com.emp.empty2.enemy.EnemyArmy;
import com.emp.empty2.enemy.EnemyShip;
import com.emp.empty2.ship.Ship;

/**
 * Created by hp on 16.08.2015.
 */
public class FireThread {
    public static void checkClash(EnemyArmy enemyarmy, Ship ship) {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < ship.shipfire.iter; j++) {
                for (int z = 0; z < 10; z++) {
                    if (enemyarmy.army[i].enemy.isClash(ship.shipfire.shipfire[j].shipfire[z].fireball)) {
                        if (!enemyarmy.army[i].isDead) {
                            enemyarmy.army[i].isCrash = GlobalValues.gametime + 2000;
                            if (ship.shipfire.isReduceBonus) {
                                enemyarmy.army[i].enemyHealth -= ship.firePower * 5;
                                GlobalValues.damageDeal += ship.firePower * 5;
                            } else {
                                enemyarmy.army[i].enemyHealth -= ship.firePower;
                                GlobalValues.damageDeal += ship.firePower;
                            }
                        }
                        ship.shipfire.shipfire[j].shipfire[z].x = -120;
                        ship.shipfire.shipfire[j].shipfire[z].y = -120;
                    }
                }
            }
        }
        for (int i = 0; i < 5; i++) {
            if (enemyarmy.army[i].enemyfire != null) {
                if (ship.ship.isClash(enemyarmy.army[i].enemyfire.fireball)) {
                    if (ship.repeltime < GlobalValues.gametime) {
                        ship.shipHp -= 3;
                        if (ship.shipHp < 0) {
                            ship.shipHp = 0;
                        }
                        ship.isCrash = GlobalValues.gametime + 2000;
                    }
                    enemyarmy.army[i].enemyfire.x = -120;
                    enemyarmy.army[i].enemyfire.y =-120;
                }
            }
        }
        for (int i = 0; i < 5; i++) {
            if (ship.ship.isClash(enemyarmy.army[i].enemy)) {
                if (ship.repeltime < GlobalValues.gametime) {
                    ship.shipHp -= GlobalValues.proctime / 15;
                    if (ship.shipHp < 0) {
                        ship.shipHp = 0;
                    }
                }
                enemyarmy.army[i].enemyHealth -= GlobalValues.proctime / 15;
                GlobalValues.damageDeal += GlobalValues.proctime / 15;
            }
        }
    }

    public static void CheckMultiPlayerClash (Ship ship, EnemyShip enemyShip) {
        for (int j = 0; j < ship.shipfire.iter; j++) {
            for (int z = 0; z < 10; z++) {
                if (enemyShip.ship.isClash(ship.shipfire.shipfire[j].shipfire[z].fireball)) {
                    enemyShip.isCrash = GlobalValues.gametime + 2000;
                    ship.shipfire.shipfire[j].shipfire[z].x = -120;
                    ship.shipfire.shipfire[j].shipfire[z].y = -120;
                }
            }
        }
        for (int j = 0; j < enemyShip.shipfire.iter; j++) {
            for (int z = 0; z < 10; z++) {
                if (ship.ship.isClash(enemyShip.shipfire.shipfire[j].shipfire[z].fireball)) {
                    ship.shipHp -= ship.firePower;
                    ship.isCrash = GlobalValues.gametime + 2000;
                    enemyShip.shipfire.shipfire[j].shipfire[z].x = -120;
                    enemyShip.shipfire.shipfire[j].shipfire[z].y = -120;
                }
            }
        }
    }
}
