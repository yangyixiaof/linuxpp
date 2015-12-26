package cn.yyx.research.linuxprogramprocessor;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import cn.yyx.research.language.Utility.BigDirectory;
import cn.yyx.research.language.Utility.SourceCodeFileIteration;

/**
 * Hello world!
 *
 */
public class PApp {

	String processdir = null;
	Thread runThread = null;
	File dir = null;
	static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

	public PApp(String processdir) {
		this.processdir = processdir;
	}

	public boolean StartProcessJavaProjects() {
		if (processdir.equals("")) {
			File f = new File("temp.txt");
			if (!f.exists()) {
				try {
					f.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			dir = new File(f.getAbsolutePath()).getParentFile();
			if (!dir.exists()) {
				System.err.println("The directory which contains this jar doesn't exist??????");
				return false;
			}
			f.delete();
		} else {
			dir = new File(processdir);
		}
		if (!dir.exists()) {
			System.err.println("Directory not exists. Dir : " + dir.getAbsolutePath());
			return false;
		}
		SourceCodeFileIteration.Initial();
		SourceCodeFileIteration.IterateAllFilesAndWriteToOneBigFile(dir);
		System.out.println("All Done......");
		return true;
	}

	public void StopProcessJavaProjects() {
		SourceCodeFileIteration.StopIterate();
	}

	public static void main(String[] args) {
		String processdir = args[0];
		if (args.length == 2) {
			BigDirectory.PrefixDirectory = args[1];
		}
		System.out.println("Java processor uses the directory : " + processdir);
		PApp app = new PApp(processdir);
		app.StartProcessJavaProjects();
	}
}