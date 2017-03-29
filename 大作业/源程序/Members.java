package com.test4;

import java.util.Vector;
import java.io.*;

class Recorder
{
	//total enemy num.
	private static int enNum=20;
	//total hero num.
	private static int myLife=3;
	//score
	private static int score=0;
	//save score
	public static FileWriter fw=null;
	public static BufferedWriter bw=null;
	//read score
	public static FileReader fr=null;
	public static BufferedReader br=null;
	//read score method
	public static void readScore()
	{
		try {
			//fr=new FileReader(f:\\Java\\score.txt);
			//br=new BufferedReader();
		} catch (Exception e) {
			// TODO: handle exception
			e.getStackTrace();
		}
	}
	//save score method
	public static void saveScore()
	{
		try {
			
			fw=new FileWriter("f:\\Java\\score.txt");
			bw=new BufferedWriter(fw);
			bw.write(score+"\r\n");
		} catch (Exception e) {
			// TODO: handle exception
			e.getStackTrace();
		}finally{
			try {
				bw.close();
				fw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static int getScore() {
		return score;
	}
	public static void setScore(int score) {
		Recorder.score = score;
	}
	public static int getEnNum() {
		return enNum;
	}
	public static void setEnNum(int enNum) {
		Recorder.enNum = enNum;
	}
	public static int getMyLife() {
		return myLife;
	}
	public static void setMyLife(int myLife) {
		Recorder.myLife = myLife;
	}
	
	//reduce enemy num.
	public static void reduceEnemy()
	{
		enNum--;
	}
	//update score(add)
	public static void addScore()
	{
		score++;
	}
	//reduce mylife
	public static void reduceMylife()
	{
		myLife--;
	}
	
}
//bomb class
class Bomb
{
	int x;
	int y;
	int life=6;
	boolean isLive=true;
	public Bomb(int x,int y)
	{
		this.x=x;
		this.y=y;
	}
	
	public void lifeDown()
	{
		if(life>0)
		{
			life--;
		}else
		{
			this.isLive=false;
		}
	}
}
//shot
class Shot implements Runnable
{
	int x;
	int y;
	int direct;
	int speed=3;
	boolean isLive=true;
	public Shot(int x,int y,int direct)
	{
		this.x=x;
		this.y=y;
		this.direct=direct;
	}
	public void run()
	{
		
		while(true)
		{
			try {
				Thread.sleep(50);
				
			} catch (Exception e) {
				// TODO: handle exception
				e.getStackTrace();
			}
			switch(direct)
			{
			case 0:
				y-=speed;
					break;
				case 1:
					x-=speed;
					break;
				case 2:
					y+=speed;
					break;
				case 3:
					x+=speed;
					break;
			}
		//	System.out.println("子弹的位置x="+this.x+"y="+this.y);
			if(x<0||y<0||x>400||y>300)
			{
				this.isLive=false;
				break;
			}
			
		}
	}
}

class Tank
{
	//表示坦克的横坐标
	int x=0;
	//坦克纵坐标
	int y=0;
	//0表示向上，1向左，2向下，3向右
	int direct=0;
	//坦克移动速度
	int speed=2;
	int speed_2=3;
	//color
	int color=0;
	boolean isLive=true;
	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public int getDirect() {
		return direct;
	}

	public void setDirect(int direct) {
		this.direct = direct;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	
	
	public Tank(int x,int y)
	{
		this.x=x;
		this.y=y;
	}
	public void moveUp()
	{
		y-=speed_2;
	}
	public void moveDown()
	{
		y+=speed_2;
	}
	public void moveLeft()
	{
		x-=speed_2;
	}
	public void moveRight()
	{
		x+=speed_2;
	}
}

//my tank
class Hero extends Tank
{
	Vector<Shot> ss=new Vector<Shot>();
	Shot s=null;
	public Hero(int x,int y)
	{
		super(x,y);
	}
	
	public void shotEnemy()
	{
		switch(this.direct)
		{
		case 0:
			s=new Shot(x+10,y,0);
			ss.add(s);
			break;
		case 1:
			s=new Shot(x,y+10,1);
			ss.add(s);
			break;
		case 2:
			s=new Shot(x+10,y+30,2);
			ss.add(s);
			break;
		case 3:
			s=new Shot(x+30,y+10,3);
			ss.add(s);
			break;
		}
		Thread t=new Thread(s);
		t.start();
	}
}

//enemy tank
//enemy move:runnable
class EnemyTank extends Tank implements Runnable
{

	
	int time=0;
	Vector<Shot> ss=new Vector<Shot>();
	//get enemytank on the panel
	Vector<EnemyTank> ets=new Vector<EnemyTank>();
	public EnemyTank(int x, int y) {
		super(x, y);
		// TODO Auto-generated constructor stub
	}
	//get enemytank on the panel
	public void setEts(Vector<EnemyTank> vv)
	{
		this.ets=vv;
	}
	public boolean isTouch()
	{
		boolean b=false;
		switch(this.direct)
		{
		case 0:
			for(int i=0;i<ets.size();i++)
			{
				EnemyTank et=ets.get(i);
				if(et!=this)
				{
					if(et.direct==0||et.direct==2)
					{
						if(this.x>et.x&&this.x<et.x+20&&this.y>et.y&&this.y<et.y+30)
						{
							return true;
						}
						if(this.x+20>et.x&&this.x+20<et.x+20&&this.y>et.y&&this.y<et.y+30)
						{
							return true;
						}
					}
					if(et.direct==1||et.direct==3)
					{
						if(this.x>et.x&&this.x<et.x+30&&this.y>et.y&&this.y<et.y+20)
						{
							return true;
						}
						if(this.x+20>et.x&&this.x+20<et.x+30&&this.y>et.y&&this.y<et.y+20)
						{
							return true;
						}
					}
				}
			}
			break;
		case 1:
			for(int i=0;i<ets.size();i++)
			{
				EnemyTank et=ets.get(i);
				if(et!=this)
				{
					if(et.direct==0||et.direct==2)
					{
						if(this.y>et.y&&this.x<et.y+30&&this.x>et.x&&this.x<et.x+20)
						{
							return true;
						}
						if(this.y+20>et.y&&this.y+20<et.y+30&&this.x>et.x&&this.x<et.x+20)
						{
							return true;
						}
					}
					if(et.direct==1||et.direct==3)
					{
						if(this.y>et.y&&this.y<et.y+20&&this.x>et.x&&this.x<et.x+30)
						{
							return true;
						}
						if(this.y+20>et.y&&this.y+20<et.y+20&&this.x>et.x&&this.x<et.x+30)
						{
							return true;
						}
					}
				}
			}
			break;
		case 2:
			for(int i=0;i<ets.size();i++)
			{
				EnemyTank et=ets.get(i);
				if(et!=this)
				{
					if(et.direct==0||et.direct==2)
					{
						if(this.x>et.x&&this.x<et.x+20&&this.y+30>et.y&&this.y+30<et.y+30)
						{
							return true;
						}
						if(this.x+20>et.x&&this.x+20<et.x+20&&this.y+30>et.y&&this.y+30<et.y+30)
						{
							return true;
						}
					}
					if(et.direct==1||et.direct==3)
					{
						if(this.x>et.x&&this.x<et.x+30&&this.y+30>et.y&&this.y+30<et.y+20)
						{
							return true;
						}
						if(this.x+20>et.x&&this.x+20<et.x+30&&this.y+30>et.y&&this.y+30<et.y+20)
						{
							return true;
						}
					}
				}
			}
			break;
		case 3:
			for(int i=0;i<ets.size();i++)
			{
				EnemyTank et=ets.get(i);
				if(et!=this)
				{
					if(et.direct==0||et.direct==2)
					{
						if(this.y>et.y&&this.x<et.y+30&&this.x+30>et.x&&this.x+30<et.x+20)
						{
							return true;
						}
						if(this.y+20>et.y&&this.y+20<et.y+30&&this.x+30>et.x&&this.x+30<et.x+20)
						{
							return true;
						}
					}
					if(et.direct==1||et.direct==3)
					{
						if(this.y>et.y&&this.y<et.y+20&&this.x+30>et.x&&this.x+30<et.x+30)
						{
							return true;
						}
						if(this.y+20>et.y&&this.y+20<et.y+20&&this.x+30>et.x&&this.x+30<et.x+30)
						{
							return true;
						}
					}
				}
			}
			break;
		}
		return b;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true)
		{
			
			switch(this.direct)
			{
			case 0:
				for(int i=0;i<30;i++)
				{
					try {
						Thread.sleep(50);
					} catch (Exception e) {
						// TODO: handle exception
						e.getStackTrace();
					}
					if(y>0&&!this.isTouch())					
					y-=speed;
				}
				break;
			case 1:
				for(int i=0;i<30;i++)
				{
					try {
						Thread.sleep(50);
					} catch (Exception e) {
						// TODO: handle exception
						e.getStackTrace();
					}
					if(x>0&&!this.isTouch())
					x-=speed;
				}
				break;
			case 2:
				for(int i=0;i<30;i++)
				{
					try {
						Thread.sleep(50);
					} catch (Exception e) {
						// TODO: handle exception
						e.getStackTrace();
					}
					if(y<270&&!this.isTouch())
					y+=speed;
				}
				break;
			case 3:
				for(int i=0;i<30;i++)
				{
					try {
						Thread.sleep(50);
					} catch (Exception e) {
						// TODO: handle exception
						e.getStackTrace();
					}
					if(x<370&&!this.isTouch())
					x+=speed;
				}
				break;
			}
			
			//decide whether we should add new shot
			time++;
			if(time%2==0)
			{
				if(isLive)
				{
					if(ss.size()<5)
					{
						Shot s=null;
						//add new shot
						switch(direct)
						{
						case 0:
							s=new Shot(x+10,y,0);
							ss.add(s);
							break;
						case 1:
							s=new Shot(x,y+10,1);
							ss.add(s);
							break;
						case 2:
							s=new Shot(x+10,y+30,2);
							ss.add(s);
							break;
						case 3:
							s=new Shot(x+30,y+10,3);
							ss.add(s);
							break;
						}
						Thread t=new Thread(s);
						t.start();
					}
				}
			}
			this.direct=(int)(Math.random()*4);
			//stop thread
			//must have this
			if(isLive==false)
			{
				break;
			}
		}
	}
	
}