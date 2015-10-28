package com.zhycheng.readerxls;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class MainActivity extends Activity {
    /** Called when the activity is first created. */
	
	ArrayList<HashMap<String,String>> al;
	ListView lv;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		try {
//			createFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
		setContentView(R.layout.main);
        lv=(ListView) findViewById(R.id.listView1);
		findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
//				String excelPath = getExcelDir()+File.separator+"demo.xls";
//				File excelFile = new File(excelPath);
//				createExcel(excelFile);
				String path =  Environment.getExternalStorageDirectory()+File.separator+"108sq"+File.separator;
				File pathFile = new File(path);;
				String name ="demo12.txt";
				if(pathFile.exists()){
					File file = new File(path+name);
					try {
						FileOutputStream out = new FileOutputStream(file);
						out.write(new String("123").getBytes());
						out.flush();
						FileWriter w = new FileWriter(file,true);
						BufferedWriter b = new BufferedWriter(w);
						b.append("测试写入");
						b.flush();
						b.close();

						InputStream input = new FileInputStream(file);
						File zipFile = new File(Environment.getExternalStorageDirectory()+File.separator+"108sq"+File.separator+"hello.zip");
						ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(
								zipFile));
						zipOut.putNextEntry(new ZipEntry(name));
						int temp = 0;
						byte[] bytes = new byte[1024];
						while((temp = input.read(bytes)) != -1){
							zipOut.write(input.read(bytes));
						}
						zipOut.flush();
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
//					createExcel(file);
				}else {
					Toast.makeText(MainActivity.this,"错误",Toast.LENGTH_SHORT).show();
				}
			}
		});
        al=new ArrayList<HashMap<String,String>>();
        AssetManager am=this.getAssets();
        InputStream is=null;
        try {
        	
			is=am.open("data.xls");
			Workbook wb=Workbook.getWorkbook(is);
			Sheet sheet=wb.getSheet(0);
			int row=sheet.getRows();
			HashMap<String,String> hm;
			for(int i=0;i<row;++i)
			{
				Cell cellarea=sheet.getCell(0, i);
				Cell cellschool=sheet.getCell(1, i);
				System.out.println(cellarea.getContents()+":"+cellschool.getContents());
				hm=new HashMap<String,String>();
				hm.put("AREA", cellarea.getContents());
				hm.put("SCHOOL", cellschool.getContents());
				al.add(hm);
			}
			SimpleAdapter sa=new SimpleAdapter(this,al,R.layout.lv_item,new String[]{"AREA","SCHOOL"},new int[]{R.id.tv_area,R.id.tv_school});
			lv.setAdapter(sa);
			 
			 
			 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


        
        
    }
	/*
	 * 创建文件
	 */
	private void createFile() throws Exception
	{
		File f=new File(Environment.getDataDirectory()+"/create.xls");
		try {
			f.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(!f.exists())
		{
			return;
		}

		WritableWorkbook wwb=Workbook.createWorkbook(f);

		WritableSheet ws=wwb.createSheet("zhycheng", 0);

		Label label=new Label(0,0,"liu");

		ws.addCell(label);

		jxl.write.Number n=new jxl.write.Number(0,1,34.56);
		ws.addCell(n);

		wwb.write();
		wwb.close();
//		File file  = new File(Environment.getExternalStorageDirectory().getPath()+"/");
		f.mkdir();
//		saveToSDCard(f);
	}
	/**
	 * 保存到SD卡
	 * @param
	 * @param
	 * @throws Exception
	 */
	public void saveToSDCard(File outFile)throws Exception{
		FileInputStream inputStream = new FileInputStream(outFile);


		File file = new File(Environment.getExternalStorageDirectory()+"/108sq","test.xls");
		FileOutputStream outStream = new FileOutputStream(file);
		byte[] b = new byte[10*1024];
		while(inputStream.read(b,0,10240) != -1){
			outStream.write(b,0,10240);
		}
//		outStream.write(file.getBytes());
		outStream.flush();
		outStream.close();

	}

	// 获取Excel文件夹
	public String getExcelDir() {
		// SD卡指定文件夹
		String sdcardPath = Environment.getExternalStorageDirectory()
				.toString();
		File dir = new File(sdcardPath + File.separator + "Excel"
				+ File.separator + "Person");
		if (dir.exists()) {
			return dir.toString();
		} else {
			dir.mkdirs();
			return dir.toString();
		}
	}
	// 创建excel表.
	public void createExcel(File file) {
		WritableSheet ws = null;
		try {
			if (!file.exists()) {// 创建表
				WritableWorkbook wwb = Workbook.createWorkbook(file);
				// 创建表单,其中sheet表示该表格的名字,0表示第一个表格,
				ws = wwb.createSheet("基本信息", 0);
				// 在指定单元格插入数据
				// 第一个参数表示,0表示第一列,第二个参数表示行,同样0表示第一行,第三个参数表示想要添加到单元格里的数据.
				Label lbl = new Label(0, 0, "档案号");
				Label bll = new Label(1, 0, "档案类型");
				Label bl2 = new Label(2, 0, "目前管理机构");
				Label bl3 = new Label(3, 0, "管理机构代码");
				Label bl4 = new Label(4, 0, "姓名");
				Label bl5 = new Label(5, 0, "性别");
				Label bl6 = new Label(6, 0, "出生日期");
				Label bl7 = new Label(7, 0, "民族");
				Label bl8 = new Label(8, 0, "证件类型");
				Label bl9 = new Label(9, 0, "证件号");
				Label bl10 = new Label(10, 0, "固定电话");
				Label bl11= new Label(11, 0, "手机号码");
				Label bll2 = new Label(12, 0, "职业");
				Label bll3 = new Label(13, 0, "家族史");
				Label bll4 = new Label(14, 0, "状态");
				Label bll5 = new Label(15, 0, "户籍地址");
				Label bll6 = new Label(16, 0, "居住地址");
				Label bll7 = new Label(17, 0, "管理对象状态");
				Label bll8 = new Label(18, 0, "建卡人");
				Label bll9 = new Label(19, 0, "建档时间");
				Label bl20 = new Label(20, 0, "身高");
				Label bl21 = new Label(21, 0, "体重");
				Label bl22 = new Label(22, 0, "腰围");
				Label bl23 = new Label(23, 0, "臀围");
				Label bl24 = new Label(24, 0, "收缩压");
				Label bl25 = new Label(25, 0, "舒张压");
				Label bl26 = new Label(26, 0, "体质指数");
				Label bl27 = new Label(27, 0, "腰臀比");
				// 添加到指定表格里.
				ws.addCell(lbl);
				ws.addCell(bll);
				ws.addCell(bl2);
				ws.addCell(bl3);
				ws.addCell(bl4);
				ws.addCell(bl5);
				ws.addCell(bl6);
				ws.addCell(bl7);
				ws.addCell(bl8);
				ws.addCell(bl9);
				ws.addCell(bl10);
				ws.addCell(bl11);
				ws.addCell(bll2);
				ws.addCell(bll3);
				ws.addCell(bll4);
				ws.addCell(bll5);
				ws.addCell(bll6);
				ws.addCell(bll7);
				ws.addCell(bll8);
				ws.addCell(bll9);
				ws.addCell(bl20);
				ws.addCell(bl21);
				ws.addCell(bl22);
				ws.addCell(bl23);
				ws.addCell(bl24);
				ws.addCell(bl25);
				ws.addCell(bl26);
				ws.addCell(bl27);
				// 创建表单,其中sheet表示该表格的名字,0表示第一个表格,
				ws = wwb.createSheet("眼保健专病情况", 1);
				// 在指定单元格插入数据
				// 第一个参数表示,0表示第一列,第二个参数表示行,同样0表示第一行,第三个参数表示想要添加到单元格里的数据.
				Label lbbl = new Label(0, 0, "档案号");
				Label bbll = new Label(1, 0, "管理机构代码");
				Label bbl2 = new Label(2, 0, "状态");
				Label bbl3 = new Label(3, 0, "眼科手术史");
				Label bbl4 = new Label(4, 0, "白内障手术");
				Label bbl5 = new Label(5, 0, "青光眼手术");
				Label bbl6 = new Label(6, 0, "其他眼病手术");
				Label bbl7 = new Label(7, 0, "是否戴镜");
				Label bbl8 = new Label(8, 0, "助视器使用");
				Label bbl9 = new Label(9, 0, "裸眼视力左");
				Label bbll0 = new Label(10, 0, "裸眼视力右");
				Label bbll1 = new Label(11, 0, "矫正视力左");
				Label bbll2 = new Label(12, 0, "矫正视力右");
				Label bbll3 = new Label(13, 0, "戴镜视力左");
				Label bbll4 = new Label(14, 0, "戴镜视力右");
				Label bbll5 = new Label(15, 0, "右眼球镜");
				Label bbll6 = new Label(16, 0, "右眼柱镜");
				Label bbll7 = new Label(17, 0, "右眼轴位");
				Label bbll8 = new Label(18, 0, "左眼球镜");
				Label bbl19 = new Label(19, 0, "左眼柱镜");
				Label bbl20 = new Label(20, 0, "左眼轴位");
				Label bbl21 = new Label(21, 0, "备注情况");
				// 添加到指定表格里.
				ws.addCell(lbbl);
				ws.addCell(bbll);
				ws.addCell(bbl2);
				ws.addCell(bbl3);
				ws.addCell(bbl4);
				ws.addCell(bbl5);
				ws.addCell(bbl6);
				ws.addCell(bbl7);
				ws.addCell(bbl8);
				ws.addCell(bbl9);
				ws.addCell(bbll0);
				ws.addCell(bbll1);
				ws.addCell(bbll2);
				ws.addCell(bbll3);
				ws.addCell(bbll4);
				ws.addCell(bbll5);
				ws.addCell(bbll6);
				ws.addCell(bbll7);
				ws.addCell(bbll8);
				ws.addCell(bbl19);
				ws.addCell(bbl20);
				ws.addCell(bbl21);
				// 创建表单,其中sheet表示该表格的名字,1表示第二个表格,
				ws = wwb.createSheet("盲人专病情况", 2);
				Label lccl = new Label(0, 0, "档案号");
				Label ccll = new Label(1, 0, "管理机构代码");
				Label ccl2 = new Label(2, 0, "状态");
				Label ccl3 = new Label(3, 0, "是否定盲");
				Label ccl4 = new Label(4, 0, "是否首发");
				Label ccl5 = new Label(5, 0, "是否再发");
				Label ccl6 = new Label(6, 0, "定盲医生");
				Label ccl7 = new Label(7, 0, "定盲日期");
				Label ccl8 = new Label(8, 0, "再发日期");
				Label ccl9 = new Label(9, 0, "发病年龄");
				Label ccl10 = new Label(10, 0, "盲和低视力分级");
				Label ccl11= new Label(11, 0, "治疗");
				Label ccll2 = new Label(12, 0, "手术日期");
				Label ccll3 = new Label(13, 0, "手术名称");
				Label ccll4 = new Label(14, 0, "术后矫正视力左");
				Label ccll5 = new Label(15, 0, "术后矫正视力右");
				Label ccll6 = new Label(16, 0, "手术需求");
				// 添加到指定表格里.
				ws.addCell(lccl);
				ws.addCell(ccll);
				ws.addCell(ccl2);
				ws.addCell(ccl3);
				ws.addCell(ccl4);
				ws.addCell(ccl5);
				ws.addCell(ccl6);
				ws.addCell(ccl7);
				ws.addCell(ccl8);
				ws.addCell(ccl9);
				ws.addCell(ccl10);
				ws.addCell(ccl11);
				ws.addCell(ccll2);
				ws.addCell(ccll3);
				ws.addCell(ccll4);
				ws.addCell(ccll5);
				ws.addCell(ccll6);

				// 创建表单,其中sheet表示该表格的名字,2表示第三个表格,
				ws = wwb.createSheet("低视力专病情况", 3);
				Label lddl = new Label(0, 0, "档案号");
				Label ddll = new Label(1, 0, "管理机构代码");
				Label ddl2 = new Label(2, 0, "状态");
				Label ddl3 = new Label(3, 0, "视力损伤开始年龄");
				Label ddl4 = new Label(4, 0, "盲和低视力分级");
				Label ddl5 = new Label(5, 0, "远视力右眼");
				Label ddl6 = new Label(6, 0, "远视力左眼");
				Label ddl7 = new Label(7, 0, "远视力双眼");
				Label ddl8 = new Label(8, 0, "近视力右眼");
				Label ddl9 = new Label(9, 0, "近视力左眼");
				Label ddl10 = new Label(10, 0, "近视力双眼");
				Label ddl11 = new Label(11, 0, "诊断");
				Label ddll2 = new Label(12, 0, "致残原因");
				Label ddll3 = new Label(13, 0, "立体视觉");
				Label ddll4 = new Label(14, 0, "色觉");
				Label ddll5 = new Label(15, 0, "眼压左眼");
				Label ddll6 = new Label(16, 0, "眼压右眼");
				Label ddll7 = new Label(17, 0, "视野左眼");
				Label ddll8 = new Label(18, 0, "视野右眼");
				Label ddll9 = new Label(19, 0, "电脑验光_右眼球镜");
				Label ddl20 = new Label(20, 0, "电脑验光_右眼柱镜");
				Label ddl21 = new Label(21, 0, "电脑验光_右眼轴位");
				Label ddl22 = new Label(22, 0, "电脑验光_左眼球镜");
				Label ddl23 = new Label(23, 0, "电脑验光_左眼柱镜");
				Label ddl24 = new Label(24, 0, "电脑验光_左眼轴位");
				Label ddl25 = new Label(25, 0, "插片验光_右眼球镜");
				Label ddl26 = new Label(26, 0, "插片验光_右眼柱镜");
				Label ddl27 = new Label(27, 0, "插片验光_右眼轴位");
				Label ddl28 = new Label(28, 0, "插片验光_左眼球镜");
				Label ddl29 = new Label(29, 0, "插片验光_左眼柱镜");
				Label ddl30 = new Label(30, 0, "插片验光_左眼轴位");
				Label ddl31 = new Label(31, 0, "矫正视力_右眼");
				Label ddl32 = new Label(32, 0, "矫正视力_左眼");
				Label ddl33 = new Label(33, 0, "矫正视力_双眼");
				// 添加到指定表格里.
				ws.addCell(lddl);
				ws.addCell(ddll);
				ws.addCell(ddl2);
				ws.addCell(ddl3);
				ws.addCell(ddl4);
				ws.addCell(ddl5);
				ws.addCell(ddl6);
				ws.addCell(ddl7);
				ws.addCell(ddl8);
				ws.addCell(ddl9);
				ws.addCell(ddl10);
				ws.addCell(ddl11);
				ws.addCell(ddll2);
				ws.addCell(ddll3);
				ws.addCell(ddll4);
				ws.addCell(ddll5);
				ws.addCell(ddll6);
				ws.addCell(ddll7);
				ws.addCell(ddll8);
				ws.addCell(ddll9);
				ws.addCell(ddl20);
				ws.addCell(ddl21);
				ws.addCell(ddl22);
				ws.addCell(ddl23);
				ws.addCell(ddl24);
				ws.addCell(ddl25);
				ws.addCell(ddl26);
				ws.addCell(ddl27);
				ws.addCell(ddl28);
				ws.addCell(ddl29);
				ws.addCell(ddl30);
				ws.addCell(ddl31);
				ws.addCell(ddl32);
				ws.addCell(ddl33);
				// 创建表单,其中sheet表示该表格的名字,2表示第三个表格,
				ws = wwb.createSheet("低视力助视器验配情况", 4);
				Label ldddl = new Label(0, 0, "档案号");
				Label dddll = new Label(1, 0, "管理机构代码");
				Label dddl2 = new Label(2, 0, "状态");
				Label dddl3 = new Label(3, 0, "双筒望远镜");
				Label dddl4 = new Label(4, 0, "双筒望远镜_配镜后右眼");
				Label dddl5 = new Label(5, 0, "双筒望远镜_配镜后左眼");
				Label dddl6 = new Label(6, 0, "双筒望远镜_配镜后双眼");
				Label dddl7 = new Label(7, 0, "单筒手持望远镜");
				Label dddl8 = new Label(8, 0, "单筒手持望远镜_配镜后右眼");
				Label dddl9 = new Label(9, 0, "单筒手持望远镜_配镜后左眼");
				Label dddl10 = new Label(10, 0, "单筒手持望远镜_配镜后双眼");
				// 添加到指定表格里.
				ws.addCell(ldddl);
				ws.addCell(dddll);
				ws.addCell(dddl2);
				ws.addCell(dddl3);
				ws.addCell(dddl4);
				ws.addCell(dddl5);
				ws.addCell(dddl6);
				ws.addCell(dddl7);
				ws.addCell(dddl8);
				ws.addCell(dddl9);
				ws.addCell(dddl10);
				// 从内存中写入文件中
//				wwb.write();
//				wwb.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}