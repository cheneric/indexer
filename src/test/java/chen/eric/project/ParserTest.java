package chen.eric.project;

import org.apache.commons.collections4.Trie;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class ParserTest {
	@Test
	public void testSearch() throws IOException {
		final Parser parser = new Parser();
		final File testDir =
			getClassPathResource("test-tree").getFile();
		parser.parseDirectory(testDir);

		Set<File> matchingFileSet = parser.search("co");
		assertEquals(2, matchingFileSet.size());

		matchingFileSet = parser.search("coni");
		assertEquals(1, matchingFileSet.size());
		final File[] matchingFilesArray = matchingFileSet.toArray(new File[0]);
		System.out.println(matchingFilesArray[0].getPath());
	}


	@Test
	public void testParseDirectory() throws IOException {
		final File testDir =
			getClassPathResource("test-tree").getFile();
		final Parser parser = new Parser();
		final Trie<String, Set<File>> trie = parser.parseDirectory(testDir);
		assertEquals(2, trie.size());
		assertEquals(2, trie.prefixMap("co").size());
		assertEquals(1, trie.prefixMap("coni").size());
	}

	@Test
	public void testParseFile() throws IOException {
		final File testFile1 =
			getClassPathResource("skilling-j 2/_sent_mail/1.")
				.getFile();
		final File testFile2 =
			getClassPathResource("skilling-j 2/_sent_mail/2.")
				.getFile();
		final Parser parser = new Parser();
		parser.parseFile(testFile1);
		final Trie<String, Set<File>> trie = parser.parseFile(testFile2);
		assertEquals(1, trie.prefixMap("<782357").size());

		Map<String,Set<File>> prefixMap = trie.prefixMap("sherri.sera@enron.com");
		assertEquals(1, prefixMap.size());
		assertTrue(prefixMap.get("sherri.sera@enron.com").contains(testFile1));
		assertTrue(prefixMap.get("sherri.sera@enron.com").contains(testFile2));
	}

	@Test
	public void testParseString() throws IOException {
		final String string1 = "cone";
		final String string2 = "cone conical";
		final String string3 = "comic";
		final File file1 = new File(string1);
		final File file2 = new File(string2);
		final File file3 = new File(string3);
		final Parser parser = new Parser();
		parser.parseString(string1, file1);
		parser.parseString(string2, file2);
		final Trie<String, Set<File>> trie = parser.parseString(string3, file3);
		assertTrue(trie.prefixMap("cod").isEmpty());

		Map<String,Set<File>> prefixMap = trie.prefixMap("co");
		assertEquals(3, prefixMap.size());
		assertTrue(prefixMap.containsKey("cone"));
		assertTrue(prefixMap.containsKey("conical"));
		assertTrue(prefixMap.containsKey("comic"));
		assertEquals(2, prefixMap.get("cone").size());
		assertTrue(prefixMap.get("cone").contains(file1));
		assertTrue(prefixMap.get("cone").contains(file2));
	}

	protected ClassPathResource getClassPathResource(String fileName) {
		return new ClassPathResource(
			ParserTest.class.getPackageName()
				.replace('.', File.separatorChar)
				+ File.separatorChar
				+ fileName);
	}
}
