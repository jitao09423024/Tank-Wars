/*
 * 1.draw tank
 * 2.move un down left right
 * shot sequentially
 * 4.hit enemy then disappear
 * 5、bomb effect
 * 6.enemy tank move  //enemytank implements runnable
 * 7.add a startPanel
 * 8.pause and restart
 * 9.record score
 */
package com.test4;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.*;

public class MyTankGame4 extends JFrame implements ActionListener{

	// game panel
	MyPanel mp=null;
	//start panel
	MystartPanel msp=null;
	
	//game menu
	JMenuBar jmb=null;
	JMenu jm1=null;
	JMenuItem jmi1=null;
	JMenuItem jmi2=null;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		MyTankGame4 mtg=new MyTankGame4();
	}

	//构造函数
	public MyTankGame4()
	{

		
		//start panel settings
		msp=new MystartPanel();
		jmb=new JMenuBar();
		jm1=new JMenu("Game(G)");
		jm1.setMnemonic('G');
		jmi1=new JMenuItem("StartNewGame(N)");
		jmi1.setMnemonic('N');
		jmi2=new JMenuItem("Exit(E)");
		jmi2.setMnemonic('E');
		jm1.add(jmi1);
		jm1.add(jmi2);
		jmb.add(jm1);
		this.setJMenuBar(jmb);
		
		//add actionlistener(new game)
		jmi1.addActionListener(this);
		jmi1.setActionCommand("newGame");
		jmi2.addActionListener(this);
		jmi2.setActionCommand("exit");
		
		Thread t2=new Thread(msp);
		t2.start();
		this.add(msp);
		this.setSize(550, 450);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getActionCommand().equals("newGame"))
		{
			mp=new MyPanel();
			Thread t=new Thread(mp);
			t.start();
			this.remove(msp);
			this.add(mp);
			this.addKeyListener(mp);
			this.setVisible(true);
			//this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		}else if(e.getActionCommand().equals("exit"))
		{
			//record score when exit
			Recorder.saveScore();
			System.exit(0);
		}
		
	}
}

//start panel
class MystartPanel extends JPanel implements Runnable{

	int times=0;
	public void paint(Graphics g)
	{
		super.paint(g);	
		g.fillRect(0, 0, 400, 300);
		if(times%2==0)
		{
			g.setColor(Color.yellow);
			Font font=new Font("华文新魏",Font.BOLD,30);
			g.setFont(font);
			g.drawString("stage 1",150, 150);
		}
		
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true)
		{
			try {
				Thread.sleep(500);
			} catch (Exception e) {
				// TODO: handle exception
				e.getStackTrace();
			}
			times++;
			this.repaint();
		}
	}
	
}

//game panel
class MyPanel extends JPanel implements KeyListener,Runnable
{
	//定义一个我的坦克
	Hero hero=null;
	EnemyTank emt=null;
	Vector<EnemyTank> ets=new Vector<EnemyTank>();
	int enemySize=10;
	
	//bomb effect
	Image image1=null;
	Image image2=null;
	Image image3=null;
	Vector<Bomb> bombs=new Vector<Bomb>();
	
	//构造函数
	public MyPanel()
	{
		hero=new Hero(100,200);
		for(int i=0;i<enemySize;i++)
		{
			emt=new EnemyTank(10+i*30,10);
			
			//enemy shot
					
			emt.setColor(0);
			emt.setDirect(2);
			//transit the enemytank on the panel to itself
			emt.setEts(ets);
			
			Thread t=new Thread(emt);
			t.start();
			ets.add(emt);
			Shot s=new Shot(emt.x+10,emt.y+30,emt.direct);
			emt.ss.add(s);
			Thread t2=new Thread(s);
			t2.start();
		}
		
//		try {
//			image1=ImageIO.read(new File("bomb_1.gif"));
//			image1=ImageIO.read(new File("bomb_1.gif"));
//			image1=ImageIO.read(new File("bomb_1.gif"));
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.getStackTrace();
//		}
		//bomb effect
		image1=Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/bomb_1.gif"));
		image2=Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/bomb_2.gif"));
		image3=Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/bomb_3.gif"));
	}
	public void paint(Graphics g)
	{
		super.paint(g);
		g.fillRect(0,0,400,300);
		
		//show information
		this.showInfo(g);
		
		//hero initialize
		if(hero.isLive)
		{
			this.drawTank(hero.getX(), hero.getY(), g, hero.direct, 1);
			
		}
		//hero bullet
		for(int i=0;i<hero.ss.size();i++)
		{
			//Shot myshot=hero.ss.get(i);
			if(hero.ss.get(i)!=null&&hero.ss.get(i).isLive==true)
			{
				g.draw3DRect(hero.ss.get(i).x, hero.ss.get(i).y, 1, 1, false);
			}
			if(hero.ss.get(i).isLive==false)
			{
				hero.ss.remove(hero.ss.get(i));
			}
		}
		//enemy tank initialize
		for(int i=0;i<ets.size();i++)
		{
			if(ets.get(i).isLive)
			{
				this.drawTank(ets.get(i).getX(), ets.get(i).getY(), g, ets.get(i).getDirect(), ets.get(i).getColor());
				for(int j=0;j<ets.get(i).ss.size();j++)
				{
			//		System.out.println("ets.get(i).ss.size()="+ets.get(i).y);
					Shot enemyshot=ets.get(i).ss.get(j);//must be j
					if(enemyshot.isLive)
					{
						g.draw3DRect(enemyshot.x, enemyshot.y, 1, 1, false);
					}
					else
					{
						ets.get(i).ss.remove(j);
					}
				}
			}
			
		}
		//draw bomb
		for(int i=0;i<bombs.size();i++)
		{
			Bomb b=bombs.get(i);
			if(b.life>4)
			{
				g.drawImage(image1, b.x, b.y, 30, 30, this);
			}else if(b.life>2)
			{
				g.drawImage(image2, b.x, b.y, 30, 30, this);
			}else if(b.life>0)
			{
				g.drawImage(image3, b.x, b.y, 30, 30, this);
			}
			
			b.lifeDown();;
			if(b.life==0)
			{
				bombs.remove(i);
			}
		}
	
	}
////hit enemy	
//	public void hitTank(Shot s,EnemyTank et)
//	{
//		switch(et.direct)
//		{
//		case 0:
//		case 2:
//			if(s.x>et.getX()&&s.x<et.getX()+20&&s.y>et.getY()&&s.y<et.getY()+30)
//			{
//				s.isLive=false;
//				et.isLive=false;
//				//bomb effect
//				Bomb b=new Bomb(et.x,et.y);
//				bombs.add(b);
//			}
//			break;
//		case 1:
//		case 3:
//			if(s.x>et.getX()&&s.x<et.getX()+30&&s.y>et.getY()&&s.y<et.getY()+20)
//			{
//				s.isLive=false;
//				et.isLive=false;
//				//bomb effect
//				Bomb b=new Bomb(et.x,et.y);
//				bombs.add(b);
//			}
//			break;
//		}
//	}
	//show information
	public void showInfo(Graphics g)
	{
		//information-down 
		this.drawTank(30, 330, g, 0, 0);
		g.setColor(Color.black);
		g.drawString(Recorder.getEnNum()+"", 60, 350);
		
		this.drawTank(90, 330, g, 0, 1);
		g.setColor(Color.black);
		g.drawString(Recorder.getMyLife()+"", 120, 350);
		//information-right
		g.setColor(Color.black);
		Font font=new Font("宋体",Font.BOLD,15);
		g.setFont(font);
		g.drawString("Your score", 410, 25);
		this.drawTank(420, 40, g, 0, 0);
		g.setColor(Color.black);
		g.drawString(Recorder.getScore()+"", 450, 60);
	}
	//bullet hit tank	
	public void hitTank(Shot s,Tank et)
	{
		switch(et.direct)
		{
		case 0:
		case 2:
			if(s.x>et.getX()&&s.x<et.getX()+20&&s.y>et.getY()&&s.y<et.getY()+30)
			{
				s.isLive=false;
				et.isLive=false;
				//bomb effect
				Bomb b=new Bomb(et.x,et.y);
				bombs.add(b);
			}
			break;
		case 1:
		case 3:
			if(s.x>et.getX()&&s.x<et.getX()+30&&s.y>et.getY()&&s.y<et.getY()+20)
			{
				s.isLive=false;
				et.isLive=false;
				//bomb effect
				Bomb b=new Bomb(et.x,et.y);
				bombs.add(b);
			}
			break;
		}
	}
	//画出坦克的函数
	public void drawTank(int x,int y,Graphics g,int direct,int type)
	{
		//判断坦克的类型
		switch(type)
		{
		case 0:
			g.setColor(Color.cyan);
			break;
		case 1:
			g.setColor(Color.yellow);
			break;
		}
		//判断坦克的方向
		switch(direct)
		{
		//向上
		case 0:
			
			//画出我的坦克（到时再封装成一个函数）
			//1.画出左边的矩形
			g.fill3DRect(x,y,5,30,false);
			g.fill3DRect(x+15,y,5,30,false);
			g.fill3DRect(x+5,y+5,10,20,false);
			g.fillOval(x+5, y+10, 10, 10);
			g.drawLine(x+10, y+15,x+10,y);
			break;
		//left
		case 1:
			g.fill3DRect(x,y,30,5,false);
			g.fill3DRect(x,y+15,30,5,false);
			g.fill3DRect(x+5,y+5,20,10,false);
			g.fillOval(x+10, y+5, 10, 10);
			g.drawLine(x+15, y+10,x,y+10);
			break;
		//down
		case 2:
			g.fill3DRect(x,y,5,30,false);
			g.fill3DRect(x+15,y,5,30,false);
			g.fill3DRect(x+5,y+5,10,20,false);
			g.fillOval(x+5, y+10, 10, 10);
			g.drawLine(x+10, y+15,x+10,y+30);
			break;
		//right
		case 3:
			g.fill3DRect(x,y,30,5,false);
			g.fill3DRect(x,y+15,30,5,false);
			g.fill3DRect(x+5,y+5,20,10,false);
			g.fillOval(x+10, y+5, 10, 10);
			g.drawLine(x+15, y+10,x+30,y+10);
			break;
		}
	}
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	//a左d右s下w上
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
		if(e.getKeyCode()==e.VK_A)
		{
			this.hero.setDirect(1);
			System.out.println("A meianxia");
			if(hero.x>0)
			{
				this.hero.moveLeft();		
			}
			System.out.println("A meianxia_2");
		}else if(e.getKeyCode()==e.VK_S)
		{
			this.hero.setDirect(2);
			if(hero.y<300)
			{
				this.hero.moveDown();
			}
		}else if(e.getKeyCode()==e.VK_D)
		{
			this.hero.setDirect(3);
			if(hero.x<400)
			{
				this.hero.moveRight();
			}
		}else if(e.getKeyCode()==e.VK_W)
		{
			this.hero.setDirect(0);
			if(hero.y>0)
			{
				this.hero.moveUp();
			}
		}
		if(e.getKeyCode()==e.VK_J)
		{
			if(hero.ss.size()<5)
			{
				this.hero.shotEnemy();
			}
		}
		this.repaint();
	}
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	public void run()
	{
		while(true)
		{
			try {
				Thread.sleep(100);
			} catch (Exception e) {
				// TODO: handle exception
				e.getStackTrace();
			}
			//hit enemytank
			for(int i=0;i<hero.ss.size();i++)
			{
				Shot shot=hero.ss.get(i);
				if(shot.isLive)
				{
					for(int j=0;j<ets.size();j++)
					{
						EnemyTank et=ets.get(j);
						if(et.isLive)
						{
							this.hitTank(shot, ets.get(j));
							if(!ets.get(j).isLive)
							{
								Recorder.reduceEnemy();
								Recorder.addScore();
							}
							
						}
					}
				}
			}
			//hit hero
			for(int i=0;i<ets.size();i++)
			{
				emt=ets.get(i);
				for(int j=0;j<emt.ss.size();j++)
				{
					Shot s=emt.ss.get(j);
					if(s.isLive)
					{
						this.hitTank(s, hero);
						if(!s.isLive)
							Recorder.reduceMylife();
					}
				}
				
			}
	
			this.repaint();;
		}
	}
}

