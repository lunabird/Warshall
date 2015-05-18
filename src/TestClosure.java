import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * ����Warshall�㷨���ɹ�ϵ�հ�
 * @author hp
 */
public class TestClosure {

	private static final String fileName = "input.txt";//��ȡ�ļ���ַ
	private static final String outputFileName = "output.txt";//д���ļ���ַ
	private static final String separator = ";";//�ָ���
	private static final int CLFS_JH = 0;//����ʽ-����
	private static final int CLFS_GX = 1;//����ʽ-��ϵ

	/**
	 * ����Warshall�㷨���ɹ�ϵ�հ�
	 * @author mrcio_s
	 * @param args
	 */
	public static void main(String[] args) {
		// ���忪ʼʱ��
		long start = System.currentTimeMillis();
		// �����ӡ�ַ���
		StringBuffer sb = new StringBuffer();
		// ��ȡ���Ϲ�ϵtxt�ļ�
		String str = readText(fileName);
		sb.append("\n��ȡ���ļ��ϼ������ϵĹ�ϵΪ��");
		sb.append("\n" + str);
		// �õ������ϵ�Ԫ��
		String[] element = (String[]) dealStr(str, CLFS_JH);
		// �õ������ϵĶ�Ԫ��ϵ
		String[][] relation = (String[][]) dealStr(str, CLFS_GX);
		// ����ϵת��Ϊ����
		int[][] M = conversionMatrix(relation, element);
		sb.append("\n��ʼ����ϵ����Ϊ��");
		sb.append("\n" + display(M, element.length));
		// �õ����ݱհ�
		M = genClosure(M, element.length);
		// ��ӡ���ݱհ�
		sb.append("\n���ɹ�ϵ�հ�����Ϊ��");
		sb.append("\n" + display(M, element.length));
		// ���հ�����ת��Ϊ��ϵ
		String returnStr = conversionRelations(M,element);
		// �õ�Ҫд����ļ�
		sb.append("\n�հ���ϵΪ��");
		sb.append("\n" + returnStr);
		// д���ļ�
		writeText(sb.toString(),outputFileName);
		// �������ʱ��
		long end = System.currentTimeMillis();
		sb.append("\nִ��ʱ��Ϊ:" + (end-start) + "����");
		// ��ӡ�ļ�
		System.out.println(sb.toString());
	}

	/**
	 * ��ȡtxt�ļ�
	 * @param fileName
	 * @return
	 */
	public static String readText(String fileName) {
		// ���ն�ȡ���ַ���
		StringBuffer sb = new StringBuffer();
		// ����ܵ���
		BufferedReader in;
		try {
			in = new BufferedReader(new FileReader(fileName));
			String line;
			while ((line = in.readLine()) != null) {
				// ���Ϻ͹�ϵ�ַ�����";"�ָ�,���ں���Ľ���
				sb.append(line).append(separator);
			}
			// �رչܵ���
			in.close();
		} catch (FileNotFoundException e) {
			System.err.println("�Ҳ�����Ӧ���ļ�,���飺" + e.getMessage());
		} catch (IOException e) {
			System.err.println("��ȡ�ļ�ʧ��,���飺" + e.getMessage());
		}
		// ���ض�ȡ�����ַ�����Ϣ
		return sb.toString();
	}

	/**
	 * ���ݴ���ʽ�����ȡ�����ַ�����Ϣ 
	 * clfsΪ0���������е�Ԫ�ط���һά�����з��� 
	 * clfsΪ1���������ϵĹ�ϵ��ÿ������Է����ά�����з���
	 * @param str
	 * @param clfs
	 * @return
	 */
	public static Object dealStr(String str, int clfs) {
		String[] returnObj = null; // �����ַ�������
		String[][] returnObjs = null; // �����ַ�����ά����

		String[] strs = str.split(separator);
		String[] temp = null;
		if (CLFS_JH == clfs) { // �������е�Ԫ��
			temp = strs[clfs].split("=");
			returnObj = temp[1].substring(1, temp[1].length() - 2).split(",");
		} else if (CLFS_GX == clfs) { // �������ϵĹ�ϵ
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
		// ���ش�����
		return clfs == 0 ? returnObj : returnObjs;
	}

	/**
	 * ����ϵת��Ϊ����
	 * @param relation
	 * @param element
	 */
	public static int[][] conversionMatrix(String[][] relation, String[] element) {
		// �������
		int[][] M = new int[element.length][element.length];
		// ����ϵת��Ϊ����
		for (int k = 0; k < relation.length; k++) {
			int i = 0, j = 0;
			boolean isExistRow = false;
			boolean isExistColumn = false;
			for (int l = 0; l < element.length; l++) {
				// �ҵ���Ӧ����
				if (relation[k][0].equals(element[l])) {
					i = l;
					isExistRow = true;
				}
				// �ҵ���Ӧ����
				if (relation[k][1].equals(element[l])) {
					j = l;
					isExistColumn = true;
				}
			}
			// ֻ�д�����,��ʱ����ֵΪ1
			if (isExistRow && isExistColumn) {
				M[i][j] = 1;
			}
		}
		// ���ؾ���
		return M;
	}

	/**
	 * �õ����ݱհ�
	 * @param str
	 * @param num
	 */
	public static int[][] genClosure(int[][] M, int N) {
		//Warshall�㷨
		for (int k = 0; k < N; k++) {
			for (int i = 0; i < N; i++) {
				for (int j = 0; j < N; j++) {
					M[i][j] = M[i][j] + M[i][k] * M[k][j];
				}
			}
		}
		
		//�������ϵ�з������ֵ��Ϊ1
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (M[i][j] == 0) {
					continue;
				} else {
					M[i][j] = 1;
				}
			}
		}
		//���·��ؾ����ϵ
		return M;
	}
	
	/**
	 * ��ʾ�����ϵ
	 * @param M
	 * @param N
	 */
	public static String display(int[][] M, int N){
		StringBuffer sb = new StringBuffer();
		// ��ʾ��ϵ����
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
	 * ������ת��Ϊ��ϵ
	 * @param relation
	 * @param element
	 */
	public static String conversionRelations(int[][] M, String[] element) {
		// ���������ַ���
		StringBuffer sb = new StringBuffer();
		sb.append("t(R)={");
		String relata = "";
		String relatb = "";
		
		//��������,ƴ�ӷ��ع�ϵ
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
		//ɾ�����Ķ���
		if(index > -1) {
			sb.deleteCharAt(index+1);
		}
		// ���ع�ϵ
		return sb.append("}").toString();
	}
	
	/**
	 * дtxt�ļ�
	 * @param str
	 * @param outputFileName
	 */
	public static void writeText(String str, String outputFileName) {
		File f = new File(outputFileName);
		try {
			f.createNewFile();//�����ļ�
			// ����ܵ���
			BufferedWriter output = new BufferedWriter(new FileWriter(f));
			// д���ļ�
			output.write(str);
			// �رչܵ���
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
