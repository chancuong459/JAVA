package GUI;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableModel;

import BUS.SanPhamBUS;
import BUS.cthdBUS;
import DTO.SanPham;
import DTO.ThongTin;
import DTO.cthd;

import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
public class TKBanHangGUI extends JPanel{
	private JTable banhangTable = new JTable();
	private JTable tongTable = new JTable();
	private DefaultTableModel model = new DefaultTableModel();
	private JScrollPane scr = new JScrollPane();
	private JTextField timkiem = new JTextField(20);
	private float tongtien;
	SanPhamBUS spBUS =new SanPhamBUS();
	cthdBUS ctBUS = new cthdBUS();
	public TKBanHangGUI() {
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		Border border = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
		
		JPanel top = new JPanel();
		JPanel mid =new JPanel();	// chứa bảng thống kê
		
		
		timkiem.setBorder(BorderFactory.createTitledBorder(border,"Tìm kiếm"));
		top.add(timkiem);
		banhangTable.setModel(model);
		model.addColumn("Mã SP");
		model.addColumn("Tên SP");
		model.addColumn("Loại SP");
		model.addColumn("Số lượng");
		model.addColumn("Đơn giá");
		model.addColumn("Doanh thu");
		
		ShowTable();
		
		scr = new JScrollPane(banhangTable);
		//mid.add(scr);
		
		
		add(top);
		add(scr);
	}
	private void ShowTable() {
		
		ArrayList<SanPham> dssp = spBUS.getDssp();
		ArrayList<cthd> dsct = ctBUS.getDssp();
		int soluong=0;
		tongtien =0;
		for(SanPham sp : dssp) {	//thiết lập listsp với mỗi sp có số lượng bằng 0
			sp.setSoluong(0);
		}
		for(SanPham sp :dssp) {	
			for(cthd ct : dsct) {
				if(sp.getMasp().equals(ct.getMaSP())) {	//nếu cùng mã thì cộng dồn số lượng bán vào
					sp.setSoluong(sp.getSoluong()+ct.getSoLuong());
				}
			}
			if(sp.getSoluong()!=0) {	//k lấy sản phẩm chưa bán
				soluong += sp.getSoluong();
				tongtien += sp.getSoluong()*sp.getDongia();
				Object[] obj= {sp.getMasp(),sp.getTensp(), sp.getMaloai(), sp.getSoluong(), sp.getDongia(), sp.getSoluong()*sp.getDongia()};
				model.addRow(obj);
			}
		}
		Object[] obj1 = {};
		model.addRow(obj1);
		Object[] obj2 = {"","","TỔNG:",soluong,"",tongtien};
		model.addRow(obj2);
		try {
			GhiFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	private void GhiFile() throws IOException{
		File FILE = new File("banhang.txt");
		FILE.delete();
		FILE.createNewFile();
		try {
			FileOutputStream file = new FileOutputStream("banhang.txt");
			DataOutputStream data = new DataOutputStream(file);
			data.writeFloat(tongtien);
			System.out.println("SAVED");
			file.close();
			data.close();
		}
		catch(FileNotFoundException e) {
			System.out.println(e);
		}
		
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JFrame f= new JFrame();
		JPanel banhang = new TKBanHangGUI();
		f.add(banhang);
		f.pack();
		f.setVisible(true);
		
	}

}
