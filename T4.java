class T1
{
	void s(final int p)
	{
		Runnable r1=new Runnable()
		{
			public void run()
			{
				System.out.println(p);
			}
		};
		Thread t1=new Thread(r1);
		t1.start();
	}
	public static void main(String args[])
	{
		new T1().s(4);
	}
}