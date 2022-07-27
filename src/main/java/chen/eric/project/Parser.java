package chen.eric.project;

import org.apache.commons.collections4.Trie;
import org.apache.commons.collections4.trie.PatriciaTrie;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Parser {
	private final Trie<String, Set<File>> trie = new PatriciaTrie<>();

	public Set<File> search(String partialTerm) {
		final Map<String,Set<File>> matchingTerms = trie.prefixMap(partialTerm);
		Set<File> allMatchingFiles = new HashSet<File>();
		for (Set<File> matchingFiles : matchingTerms.values()) {
			allMatchingFiles.addAll(matchingFiles);
		}
		return allMatchingFiles;
	}

	public Trie<String, Set<File>> parseDirectory(File directory) throws IOException {
		final Queue<File> nextDirectories = new LinkedList<>();
		nextDirectories.add(directory);
		File currentDirectory;
		while ((currentDirectory = nextDirectories.poll()) != null) {
			if (currentDirectory.isDirectory()) {
				final File[] files = currentDirectory.listFiles();
				for (final File file : files) {
					if (file.isDirectory()) {
						nextDirectories.add(file);
					}
					else {
						parseFile(file);
					}
				}
			}
			else {
				parseFile(currentDirectory);
			}
		}
		return trie;
	}

	protected Trie<String, Set<File>> parseFile(File file) throws IOException {
		final BufferedReader reader = new BufferedReader(new FileReader(file));
		String line;
		while ((line = reader.readLine()) != null) {
			parseString(line, file);
		}
		return trie;
	}

	protected Trie<String, Set<File>> parseString(String string, File file) {
		final String[] words = string.split("\\s+");
		Arrays.stream(words)
			.forEach(word -> addToTrie(word, file));
		return trie;
	}

	protected void addToTrie(String key, File file) {
		Set<File> files = trie.get(key);
		if (files == null) {
			files = new HashSet<>();
			trie.put(key, files);
		}
		files.add(file);
	}
}
