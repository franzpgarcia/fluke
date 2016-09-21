package fluke.common

import java.nio.ByteBuffer;

import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;

import com.google.common.io.GwtWorkarounds.ByteOutput;

class TarCompressor {

	def tar(InputStream stream, String name) {
		ByteArrayOutputStream inOutStream = new ByteArrayOutputStream()
		IOUtils.copy(stream, inOutStream)

		ByteArrayOutputStream outStream = new ByteArrayOutputStream()
		ArchiveOutputStream tarOutStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.TAR, outStream);

		TarArchiveEntry entry = new TarArchiveEntry(name)
		entry.size = inOutStream.buf.size()
		tarOutStream.putArchiveEntry(entry)
		IOUtils.copy(new ByteArrayInputStream(inOutStream.buf), tarOutStream)
		tarOutStream.closeArchiveEntry()
		tarOutStream.close()
		
		return new ByteArrayInputStream(outStream.buf)
	}

	def tar(String src) {
		File file = new File(src)
		if(!file.exists()) {
			throw new FileNotFoundException("Could not find file or directory at ${src}")
		}
		ByteArrayOutputStream outStream = new ByteArrayOutputStream()
		ArchiveOutputStream tarOutStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.TAR, outStream);

		if(file.isDirectory()) {
			tarDirectory(tarOutStream, file, null)
		} else {
			tarFile(tarOutStream, file, null)
		}

		tarOutStream.close()
		return new ByteArrayInputStream(outStream.buf)
	}

	private void tarDirectory(ArchiveOutputStream tarOutStream, File file, String path) {
		if(path) {
			TarArchiveEntry dirEntry = new TarArchiveEntry(file, path)
			tarOutStream.putArchiveEntry(dirEntry)
			tarOutStream.closeArchiveEntry()
		}
		for(File subFile : file.listFiles()) {
			if(subFile.isDirectory()) {
				tarDirectory(tarOutStream, subFile, (path?:"") + "/" + subFile.name)
			} else {
				tarFile(tarOutStream, subFile, (path?:""))
			}
		}
	}
	
	private void tarFile(ArchiveOutputStream tarOutStream, File file, String path) {
		TarArchiveEntry entry = new TarArchiveEntry(file, (path?:"") + "/" + file.name)
		tarOutStream.putArchiveEntry(entry)
		IOUtils.copy(new FileInputStream(file), tarOutStream)
		tarOutStream.closeArchiveEntry()
	}
}
