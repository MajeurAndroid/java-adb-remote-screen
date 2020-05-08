package com.majeur.ars.adb;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class CommandExec {

	interface Callback {
		void callback(byte[] data);
	}

	static void execAsync(final List<String> command, final Callback callback) {
		new Thread(() -> {
			byte[] data = exec(command);
			if (callback != null)
				callback.callback(data);
		}).start();
	}

	static byte[] exec(List<String> command) {
		//Logger.d(Arrays.toString(command.toArray()));
		ProcessBuilder builder = new ProcessBuilder(command);
		builder.redirectErrorStream(true);
		Process process;
		try {
			process = builder.start();

			InputStream inputStream = process.getInputStream();
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();

			int nRead;
			byte[] dataChunk = new byte[16384];
			while ((nRead = inputStream.read(dataChunk, 0, dataChunk.length)) != -1)
				buffer.write(dataChunk, 0, nRead);
			buffer.flush();

			byte[] data = buffer.toByteArray();

			buffer.close();
			inputStream.close();

			return data;

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

}
