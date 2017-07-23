import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

import static java.lang.Math.*;

/**
 * Created by LightDance on 2017/7/4.
 */
public class Ship {
    private double shipEnergy;                                                //轨道能量
    private double shipVelocity, shipVelocityX, shipVelocityY;             //速度大小和分量
    private double shipVelocityR, shipVelocityU;                            //速度另一种形式分量
    private double shipAverageAngularVelocity,shipAverageVelocity;         //平均线速度和平均角速度
    private double shipR,shipRX,shipRY;                                      //径矢大小和分量
    private double shipP,shipT=40;                                           //飞船周期和返回数据的时间间隔
    private double shipe,shipa,shipb,shipp;                                 //偏心率e，半长轴a，半通径p
    private double shipAngleE,shipAnglef;                                   //偏近点角E，真近点角f
    private double shipAngleθ;                                              //径矢与水平方向的夹角  *注*需计算一下，用于表示角度变化
    private double μ;                                                        //常数，星球的属性，需要星球提供的参数，类似于万有引力公式中GM常数项
    private double shipPositionX,shipPositionY;                            //飞船在坐标系中的位置
    private double shipH;                                                     //飞船动量矩H
    private double shipFu,shipFx,shipFy;                                    //飞船垂直径矢方向受力大小,xy轴上分量
    private double shipω;                                                    //轨道要素ω
    private Planet surroundPlanet;                                            //绕转星球，用于获取参数、绘制轨道

    //游戏开始时的初始化
    Ship(){
        surroundPlanet=new Planet(395,395,
                10,400000);             //暂时先这样，后期应有get方法获取绕转的星球
        μ=surroundPlanet.getPlanetμ();
        shipRX=200;//R=250,Rx=-200,Ry=150；V=30,Vx=-18,Vy=24时，飞船不在轨道上
        shipRY=150;
        shipR=getShipR(shipRX,shipRY);
        shipVelocityX =18;
        shipVelocityY =-24;                              //以上均为已知条件
        shipVelocity = getShipVelocity(shipVelocityX, shipVelocityY);
        shipH=getShipH(shipRX,shipRY, shipVelocityX, shipVelocityY);
        shipEnergy=getShipEnergy(shipVelocity,shipR,μ);
        shipa=getShipa(shipEnergy,μ);
        shipp=getShipp(shipH,μ);
        shipe=getShipe(shipp,shipa);
        shipb=getShipb(shipa,shipe);
        shipAnglef=getShipAnglef(shipH,shipe,shipR,μ);
        shipω=getShipω(shipAnglef);
        shipAngleE=getShipAngleE(shipR,shipa,shipe);
        shipAngleθ=getShipAngleθ(shipRX,shipRY,shipAnglef);
    }

    //初始化过程中的相关方法，计算飞船的各个参数
    private double getShipR(double shipRX,double shipRY){
        double res=sqrt(shipRX*shipRX+shipRY*shipRY);
        System.out.println("R="+res);
        return res;
    }

    private double getShipVelocity(double shipVelocityX, double shipVelocityY){
        double res=sqrt(shipVelocityX*shipVelocityX+shipVelocityY*shipVelocityY);
        System.out.println("V="+res);
        return res;
    }

    private double getShipH(double shipRX,double shipRY,double shipVelocityX,double shipVelocityY){
        double res=-shipRY*shipVelocityX+shipRX*shipVelocityY;
        System.out.println("H="+res);
        return res;
    }

    private double getShipEnergy(double shipVelocity,double shipR,double μ){
        double res=0.5*shipVelocity*shipVelocity-μ/shipR;
        System.out.println("E="+res);
        return res;
    }
    //这里需要使a大于0，所以设置参数时需特别注意
    private double getShipa(double shipEnergy,double μ){
        double res=-μ/(2*shipEnergy);
        System.out.println("a="+res);
        return res;
    }

    private double getShipp(double shipH,double μ){
        double res=shipH*shipH/μ;
        System.out.println("p="+res);
        return res;
    }
    //这里以后会有bug，因轨道形状并不一定是椭圆，根号下可能会是负数
    private double getShipe(double shipp,double shipa){
        double res=sqrt(1-shipp/shipa);
        System.out.println("e="+res);
        return res;
    }

    private double getShipAnglef(double shipH,double shipe,double shipR,double μ){
        double res=acos((shipH*shipH-μ*shipR)/(μ*shipe*shipR));
        System.out.println("f="+res);
        return res;
    }

    private double getShipω(double shipAnglef){
        double res=-shipAnglef;
        System.out.println("ω="+res);
        return res;
    }

    private double getShipAngleE(double shipR,double shipa,double shipe){
        double res=acos((shipa-shipR)/(shipa*shipe));
        System.out.println("∠E="+res);
        return res;
    }

    private double getShipAngleθ(double shipRX,double shipRY,double shipAnglef){
        double res=atan(shipRY/shipRX)+Math.PI-shipAnglef;
        System.out.println("θ="+res);
        return res;
    }

    private double getShipb(double shipa,double shipe){
        double res=sqrt(shipa*shipa-shipa*shipa*shipe*shipe);
        System.out.println("b="+res);
        return res;
    }

    //绘制Kepler轨道的相关方法
    public Ellipse2D drawKeplerTrace(){
        //x坐标有bug暂时打算用 速度大小与平均速度比较来判断离近地点近还是离远地点近确定+c还是-c来解决
        Ellipse2D shipTrace=new Ellipse2D.Double(
                surroundPlanet.getPlanetPositionX()-shipa+shipa*shipe,
                surroundPlanet.getPlanetPositionY()-sqrt(abs(shipa*shipa-shipa*shipa*shipe*shipe)),
                shipa*2,sqrt(abs(shipa*shipa-shipa*shipa*shipe*shipe))*2);
        return shipTrace;
    }
    //返回一个矩形到飞船位置
    public Rectangle2D drawShipPosition(){
        Rectangle2D shipPosition=new Rectangle2D.Double(
                surroundPlanet.getPlanetPositionX()+shipRX-5,
                surroundPlanet.getPlanetPositionY()+shipRY-5,
                10,10);
        return shipPosition;
    }
    //返回一个e向量与水平的夹角用于旋转轨道
    public double getShipAngleθ(){
        return shipAngleθ;
    }
    //返回一个速度方向用于使船头朝向速度方向
    public double getShipDirection(){
        return atan(shipVelocityY / shipVelocityX);
    }

    //返回飞船坐标
    public double getShipX(){
        return surroundPlanet.getPlanetPositionX()+shipRX-5;
    }
    public double getShipY(){
        return surroundPlanet.getPlanetPositionY()+shipRY-5;
    }



    //根据参数得出椭圆的方程，然后根据速度大小控制运动的快慢


    public void shipMoving(){
        double x,X,y,Y;                                                       //存放三角函数形式的椭圆轨迹
        x=surroundPlanet.getPlanetPositionX()+shipa*cos(shipAngleE);
        y=surroundPlanet.getPlanetPositionY()+shipb*sin(shipAngleE);
        X=x*cos(shipAngleE+shipAngleθ)-y*sin(shipAngleE+shipAngleθ);
        Y=x*sin(shipAngleE+shipAngleθ)+y*cos(shipAngleE+shipAngleθ);    //表示出椭圆轨道三角函数方程
        if (shipH>0){                                                         //逆时针方向运动
            shipP=getShipP(shipa,shipb,shipH);
            shipAverageVelocity=getShipAverageVelocity(shipa,μ);
            shipAverageAngularVelocity =getShipAverageAngularVelocity(shipP);
            shipAngleE-=shipAverageAngularVelocity*shipVelocity/shipAverageVelocity*shipT/100000;
            shipR=shipa*(1-shipe*cos(shipAngleE));
            shipRX=shipR*cos(shipAngleE+shipAngleθ);
            shipRY=shipR*sin(shipAngleE+shipAngleθ);
            shipVelocityR=sqrt(μ/shipp)*shipe*sin(shipAnglef);
            shipVelocityU=sqrt(μ/shipp)*(1+shipe*cos(shipAnglef));
            shipVelocity=sqrt(shipVelocityR*shipVelocityR+shipVelocityU*shipVelocityU);
            shipVelocityX=shipVelocityR*cos(PI-shipAngleθ+shipAngleE)
                    -shipVelocityU*sin(PI-shipAngleθ+shipAngleE);
            shipVelocityY=shipVelocityR*sin(PI-shipAngleθ+shipAngleE)
                    +shipVelocityU*cos(PI-shipAngleθ+shipAngleE);

        }else if (shipH<0){                                                         //逆时针方向运动
            shipP=getShipP(shipa,shipb,-shipH);
            shipAverageVelocity=getShipAverageVelocity(shipa,μ);
            shipAverageAngularVelocity =getShipAverageAngularVelocity(shipP);
            shipAngleE+=shipAverageAngularVelocity*shipVelocity/shipAverageVelocity*shipT/10000;
            shipR=shipa*(1-shipe*cos(shipAngleE));
            shipRX=shipR*cos(shipAngleE+shipAngleθ);
            shipRY=shipR*sin(shipAngleE+shipAngleθ);
            shipVelocityR=sqrt(μ/shipp)*shipe*sin(shipAnglef);
            shipVelocityU=sqrt(μ/shipp)*(1+shipe*cos(shipAnglef));
            shipVelocity=sqrt(shipVelocityR*shipVelocityR+shipVelocityU*shipVelocityU);
            shipVelocityX=shipVelocityR*cos(PI-shipAngleθ+shipAngleE)
                    -shipVelocityU*sin(PI-shipAngleθ+shipAngleE);
            shipVelocityY=shipVelocityR*sin(PI-shipAngleθ+shipAngleE)
                    +shipVelocityU*cos(PI-shipAngleθ+shipAngleE);
        }



    }



    //飞船中心引力运动所用到的方法
    private double getShipP(double shipa,double shipb,double shipH){
        double res=2*PI*shipa*shipb/shipH;//H的正负未考虑
        System.out.println("P="+res);
        return res;
    }

    private double getShipAverageVelocity(double shipa,double μ){
        double res=sqrt(μ/shipa/shipa/shipa);
        System.out.println("平均v="+res);
        return res;
    }

    private  double getShipAverageAngularVelocity(double shipP){
        return 2*PI/shipP;
    }








































////仍然用物理方法表示运动的一种思路
//
//    //飞船中心引力运动
//
//    private void shipMoving(){
//        if (shipH>0){
//            shipP=getShipP(shipa,shipb,shipH);
//            shipAverageVelocity=getShipAverageVelocity(shipa,μ);
//            shipAverageAngularVelocity =getShipAverageAngularVelocity(shipP);
//            shipAngleθ_1=shipAngleθ- shipAverageAngularVelocity *shipVelocity/shipAverageVelocity*shipT;
//            if (shipAngleθ<2*PI){
//                shipAngleθ+=2*PI;           //控制角度大小，放置累加累减导致超范围
//            }
//            shipR=shipRChangedForMoving(shipAngleθ,shipAngleθ_1,shipH);
//
//        }else if(shipH<0){
//            shipP=getShipP(shipa,shipb,-shipH);
//            shipAverageVelocity=getShipAverageVelocity(shipa,μ);
//            shipAverageAngularVelocity =getShipAverageAngularVelocity(shipP);
//            shipAngleθ+= shipAverageAngularVelocity *shipVelocity/shipAverageVelocity*shipT;
//            if (shipAngleθ>2*PI){
//                shipAngleθ-=2*PI;
//            }
//
//
//        }
//    }
//
//
//    private double getShipP(double shipa,double shipb,double shipH){
//        double res=2*PI*shipa*shipb/shipH;//H的正负未考虑
//        System.out.println("P="+res);
//        return res;
//    }
//
//    private double getShipAverageVelocity(double shipa,double μ){
//        double res=sqrt(μ/shipa/shipa/shipa);
//        System.out.println("平均v="+res);
//        return res;
//    }
//
//    private  double getShipAverageAngularVelocity(double shipP){
//        return 2*PI/shipP;
//    }



}

















