import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.applet.*;
import java.net.*;
import java.beans.*;
import java.util.regex.*;
import java.awt.dnd.*;
import java.awt.datatransfer.*;
import javax.swing.event.*;
import java.text.*;
import locations.*;
public class ExtendedSearch implements ProgressListener,ListCellRenderer<Object>
{
	private static class ModelContent
	{
		private String value;
		private String desc;
		private Color color=Color.white;
		public ModelContent(String value)
		{
			this(value,null,Color.white);
		}
		public ModelContent(String value,String desc,Color color)
		{
			this.value=value;
			this.desc=desc;
			this.color=color;
		}
		public String getValue()
		{
			return value;
		}
		public String getDesc()
		{
			return desc;
		}
		public Color getColor()
		{
			return color;
		}
		public void setValue(String value)
		{
			this.value=value;
		}
		public void setColor(Color color)
		{
			this.color=color;
		}
		public void setDesc(String desc)
		{
			this.desc=desc;
		}
	};
	private JFrame f1=new JFrame("ExtendedSearch");
	private JPanel p1=new JPanel();
	private JList l1;
	private DefaultListModel<ModelContent> dModel1=new DefaultListModel<>();
	private JTextField t1=new JTextField("Martin Scorsese");
	private JLabel t2=new JLabel("Drag and drop more items to the list below:");
	private JProgressBar pb1=new JProgressBar();
	private JLabel status=new JLabel();
	private DropTarget dTarget;
	private TransferHandler tHandler=new TransferHandler(null)
	{
		public boolean canImport(TransferHandler.TransferSupport info)
		{
			if(!(info.isDataFlavorSupported(DataFlavor.stringFlavor) || 
				info.isDataFlavorSupported(DataFlavor.javaFileListFlavor)))
				return false;
			else
				return true;
		}
		public int getSourceActions(JComponent k)
		{
			return TransferHandler.COPY;
		}
		public boolean importData(TransferHandler.TransferSupport info)
		{
			if(!info.isDrop())
				return false;
			JList.DropLocation d1=(JList.DropLocation)info.getDropLocation();
			int index=d1.getIndex();
			Transferable tr=info.getTransferable();
			try
				{
			if(tr.isDataFlavorSupported(DataFlavor.stringFlavor))
			{
				String data=(String)tr.getTransferData(DataFlavor.stringFlavor);
				String list[]=data.split("\n");
				for(int i=0;i<list.length;i++)
				dModel1.add(i,new ModelContent(list[i]));
			}
			else if(tr.isDataFlavorSupported(DataFlavor.javaFileListFlavor))
			{
				java.util.List<File> fileList=(java.util.List<File>)tr.getTransferData(DataFlavor.javaFileListFlavor);
				for(int i=0;i<fileList.size();i++)
					dModel1.add(i,new ModelContent(fileList.get(i).getName()));
			}
				}
			catch(UnsupportedFlavorException ufexcepn){}
			catch(IOException ioexcepn){}
			
			return true;
		}
	};
	public static String[] getContents(String file)throws IOException
	{
		ArrayList<String> r=new ArrayList<String>();
		BufferedReader br1=new BufferedReader(new FileReader(file));
		for(String line=br1.readLine();line!=null;line=br1.readLine())
			r.add(line);
		br1.close();
		return r.toArray(new String[r.size()]);
	}
public ExtendedSearch()
{
	try
	{
	String sampleList[]=getContents("SampleList.txt");
	for(String s : sampleList)
		dModel1.add(0,new ModelContent(s));
}
catch(IOException ioexcepn){}
	l1=new JList(dModel1);
	l1.setDropMode(DropMode.ON);
	l1.setTransferHandler(tHandler);
addComponents();
}
public void onStatusChange(String msg,int prog)
{
	SwingUtilities.invokeLater(new Runnable()
	{
		public void run()
		{
	status.setText(msg);
	pb1.setValue(prog);
		}
	});
}
private void addComponents()
{
	l1.setDragEnabled(true);
	l1.setCellRenderer(this);
	p1.add(l1);
	f1.add(p1);
	f1.setLayout(new BorderLayout());
	f1.setAlwaysOnTop(true);
	f1.addWindowListener(new WindowAdapter()
	{
		public void windowClosing(WindowEvent e)
		{
			System.exit(0);
		}
	});
	JScrollPane sp1=new JScrollPane(l1,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
		JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	sp1.setPreferredSize(new Dimension(350,200));
	p1.add(sp1);
	f1.setLocation(200,200);
	f1.add(p1,BorderLayout.CENTER);
	JPanel p2=new JPanel();
	p2.setLayout(new GridLayout(1,0));
	p2.add(t1);
	p2.add(t2);
	f1.add(p2,BorderLayout.NORTH);
	JPanel p4=new JPanel();
	p4.setLayout(new GridLayout(0,1));
	p4.add(pb1);
	p4.add(status);
	JButton empty=new JButton("Empty"),	b1=new JButton("Search");
	p4.add(empty);
	empty.addActionListener(new ActionListener()
	{
		public void actionPerformed(ActionEvent e)
		{
			dModel1.clear();
		}
	});
	p4.add(b1);
	f1.add(p4,BorderLayout.SOUTH);
	b1.addActionListener(new ActionListener()
	{
		public void actionPerformed(ActionEvent e)
		{
			WikipediaFind f;
			f=new WikipediaFind();
			for(int i=0;i<dModel1.size();i++)
			{
				f.addToList(dModel1.elementAt(i).getValue());	
			}
			try
			{
				Thread thrd1=new Thread(new Runnable()
				{
					public void run()
					{
						try
						{
			System.out.println(f.search(new SimpleTextSearcher(),t1.getText(),1,ExtendedSearch.this));
	}
	catch(Exception e){}
			}
		});
				thrd1.start();
		}
		catch(Throwable t)
		{
			t.printStackTrace();
		}
		}
	});
	f1.pack();
	f1.setVisible(true);
}
public static void main(String args[])
{
	ExtendedSearch es=new ExtendedSearch();
}
public void resultGenerated(final ResultDetails rd1,final int pos,String srchHint)
{
	SwingUtilities.invokeLater(new Runnable()
	{
		public void run()
		{
		/*	if(dModel1.size()>pos+1)
			pb1.setString(dModel1.elementAt(pos+1));
			pb1.setValue((pos*100)/dModel1.size());*/
			Color bk=Color.white;
			ModelContent k=dModel1.elementAt(pos);
			if(rd1.getValidity()==ResultDetails.VALID)
			{
			k.setColor(Color.green);
			k.setDesc("__VALID__");
			}
			else if(rd1.getValidity()==ResultDetails.INVALID)
			{
			k.setColor(Color.red);
			k.setDesc("__INVALID__");
			}
			else
			{
				k.setColor(Color.yellow);
				k.setDesc("__AMBIGUOUS__");
			}
			System.out.println(rd1+" "+pos+" "+dModel1.elementAt(pos));
			l1.repaint();
//			progs.get(dModel1.elementAt(pos)).setValue(100);
		}
	});
}
public void taskCompleted()
{
	status.setText("Completed");
}
public void onError(String e)
{
	status.setText(e);
}
public Component getListCellRendererComponent(JList<?> list,Object value,int index,boolean isSelected,boolean cellHasFocus)
{
	ModelContent k=(ModelContent)value;
	JPanel p1=new JPanel();
	p1.setLayout(new GridBagLayout());
	p1.add(new JLabel(k.getValue()));
	if(k.getDesc()!=null)
	p1.add(new JLabel(k.getDesc()));
	p1.setBackground(k.getColor());
	p1.repaint();
	return p1;
}
};
