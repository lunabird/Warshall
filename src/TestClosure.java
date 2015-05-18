import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 测试Warshall算法生成关系闭包
 * @author hp
 */
public class TestClosure {

	private static final String fileName = "input.txt";//读取文件地址
	private static final String outputFileName = "output.txt";//写入文件地址
	private static final String separator = ";";//分隔符
	private static final int CLFS_JH = 0;//处理方式-集合
	private static final int CLFS_GX = 1;//处理方式-关系

	/**
	 * 测试Warshall算法生成关系闭包
	 * @author mrcio_s
	 * @param args
	 */
	public static void main(String[] args) {
		// 定义开始时间
		long start = System.currentTimeMillis();
		// 定义打印字符串
		StringBuffer sb = new StringBuffer();
		// 读取集合关系txt文件
		String str = readText(fileName);
		sb.append("\n读取到的集合及集合上的关系为：");
		sb.append("\n" + str);
		// 得到集合上的元素
		String[] element = (String[]) dealStr(str, CLFS_JH);
		// 得到集合上的二元关系
		String[][] relation = (String[][]) dealStr(str, CLFS_GX);
		// 将关系转换为矩阵
		int[][] M = conversionMatrix(relation, element);
		sb.append("\n初始化关系矩阵为：");
		sb.append("\n" + display(M, element.length));
		// 得到传递闭包
		M = genClosure(M, element.length);
		// 打印传递闭包
		sb.append("\n生成关系闭包矩阵为：");
		sb.append("\n" + display(M, element.length));
		// 将闭包矩阵转换为关系
		String returnStr = conversionRelations(M,element);
		// 得到要写入的文件
		sb.append("\n闭包关系为：");
		sb.append("\n" + returnStr);
		// 写入文件
		writeText(sb.toString(),outputFileName);
		// 定义结束时间
		long end = System.currentTimeMillis();
		sb.append("\n执行时间为:" + (end-start) + "毫秒");
		// 打印文件
		System.out.println(sb.toString());
	}

	/**
	 * 读取txt文件
	 * @param fileName
	 * @return
	 */
	public static String readText(String fileName) {
		// 接收读取的字符串
		StringBuffer sb = new StringBuffer();
		// 定义管道流
		BufferedReader in;
		try {
			in = new BufferedReader(new FileReader(fileName));
			String line;
			while ((line = in.readLine()) != null) {
				// 集合和关系字符串以";"分割,便于后面的解析
				sb.append(line).append(separator);
			}
			// 关闭管道流
			in.close();
		} catch (FileNotFoundException e) {
			System.err.println("找不到对应的文件,请检查：" + e.getMessage());
		} catch (IOException e) {
			System.err.println("读取文件失败,请检查：" + e.getMessage());
		}
		// 返回读取到的字符串信息
		return sb.toString();
	}

	/**
	 * 根据处理方式处理读取到的字符串信息 
	 * clfs为0：将集合中的元素放入一维数组中返回 
	 * clfs为1：将集合上的关系中每个有序对放入二维数组中返回
	 * @param str
	 * @param clfs
	 * @return
	 */
	public static Object dealStr(String str, int clfs) {
		String[] returnObj = null; // 返回字符串数组
		String[][] returnObjs = null; // 返回字符串二维数组

		String[] strs = str.split(separator);
		String[] temp = null;
		if (CLFS_JH == clfs) { // 处理集合中的元素
			temp = strs[clfs].split("=");
			returnObj = temp[1].substring(1, temp[1].length() - 2).split(",");
		} else if (CLFS_GX == clfs) { // 处理集合上的关系
			temp = strs[clfs].split("=");
			String[] tempStr = temp[1].substring(2, temp[1].length() - 3)
					.split(">,<");
			returnObjs = new String[tempStr.length][2];
			for (int i = 0; i < tempStr.length; i++) {
				String s = tempStr[i];
				returnObjs[i][0] = s.split(",")[0];
				returnObjs[i][1] = s.split(",")[1];
			}
		}
		// 返回处理结果
		return clfs == 0 ? returnObj : returnObjs;
	}

	/**
	 * 将关系转换为矩阵
	 * @param relation
	 * @param element
	 */
	public static int[][] conversionMatrix(String[][] relation, String[] element) {
		// 定义矩阵
		int[][] M = new int[element.length][element.length];
		// 将关系转换为矩阵
		for (int k = 0; k < relation.length; k++) {
			int i = 0, j = 0;
			boolean isExistRow = false;
			boolean isExistColumn = false;
			for (int l = 0; l < element.length; l++) {
				// 找到对应的行
				if (relation[k][0].equals(element[l])) {
					i = l;
					isExistRow = true;
				}
				// 找到对应的列
				if (relation[k][1].equals(element[l])) {
					j = l;
					isExistColumn = true;
				}
			}
			// 只有存在行,列时才置值为1
			if (isExistRow && isExistColumn) {
				M[i][j] = 1;
			}
		}
		// 返回矩阵
		return M;
	}

	/**
	 * 得到传递闭包
	 * @param str
	 * @param num
	 */
	public static int[][] genClosure(int[][] M, int N) {
		//Warshall算法
		for (int k = 0; k < N; k++) {
			for (int i = 0; i < N; i++) {
				for (int j = 0; j < N; j++) {
					M[i][j] = M[i][j] + M[i][k] * M[k][j];
				}
			}
		}
		
		//将矩阵关系中非零的数值置为1
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (M[i][j] == 0) {
					continue;
				} else {
					M[i][j] = 1;
				}
			}
		}
		//重新返回矩阵关系
		return M;
	}
	
	/**
	 * 显示矩阵关系
	 * @param M
	 * @param N
	 */
	public static String display(int[][] M, int N){
		StringBuffer sb = new StringBuffer();
		// 显示关系矩阵
		for (int i = 0; i < N; i++) {
			sb.append("\t");
			for (int j = 0; j < N; j++) {
				sb.append(M[i][j]+ "  ");
			}
			sb.append("\n");
		}
		return sb.toString();
	}
	
	/**
	 * 将矩阵转换为关系
	 * @param relation
	 * @param element
	 */
	public static String conversionRelations(int[][] M, String[] element) {
		// 定义连接字符串
		StringBuffer sb = new StringBuffer();
		sb.append("t(R)={");
		String relata = "";
		String relatb = "";
		
		//遍历矩阵,拼接返回关系
		for (int i = 0; i < element.length; i++) {
			for (int j = 0; j < element.length; j++) {
				if (M[i][j] == 0) {
					continue;
				} else {
				    relata = element[i];
					relatb = element[j];
				}
				sb.append("<").append(relata).append(",").append(relatb).append(">").append(",");
			}
		}
		int index = sb.lastIndexOf(">,");
		//删除最后的逗号
		if(index > -1) {
			sb.deleteCharAt(index+1);
		}
		// 返回关系
		return sb.append("}").toString();
	}
	
	/**
	 * 写txt文件
	 * @param str
	 * @param outputFileName
	 */
	public static void writeText(String str, String outputFileName) {
		File f = new File(outputFileName);
		try {
			f.createNewFile();//创建文件
			// 定义管道流
			BufferedWriter output = new BufferedWriter(new FileWriter(f));
			// 写入文件
			output.write(str);
			// 关闭管道流
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
