package htsjdk.samtools;

import java.io.File;
import java.io.IOException;

/**
 * @author mccowan
 */
public class SamFiles {
    /**
     * Finds the index file associated with the provided SAM file.  The index file must exist and be reachable to be found.
     *
     * If the file is a symlink and the index cannot be found, try to unsymlink the file and look for the bai in the actual file path.
     *
     * @return The index for the provided SAM, or null if one was not found.
     */
    public static File findIndex(final File samFile){
        final File indexFile = lookForIndex(samFile); //try to find the index
        if(indexFile == null){
            try {
                return lookForIndex(samFile.getCanonicalFile()); //if the index didn't exist try to unsymlink the file and try at the real file path
            } catch (IOException e){
                return null;
            }
        } else {
            return indexFile;
        }
    }

    private static File lookForIndex(final File samFile) {// If input is foo.bam, look for foo.bai
        File indexFile;
        final String fileName = samFile.getName();
        if (fileName.endsWith(BamFileIoUtils.BAM_FILE_EXTENSION)) {
            final String bai = fileName.substring(0, fileName.length() - BamFileIoUtils.BAM_FILE_EXTENSION.length()) + BAMIndex.BAMIndexSuffix;
            indexFile = new File(samFile.getParent(), bai);
            if (indexFile.isFile()) {
                return indexFile;
            }
        }

        // If foo.bai doesn't exist look for foo.bam.bai
        indexFile = new File(samFile.getParent(), samFile.getName() + BAMIndex.BAMIndexSuffix);
        if (indexFile.isFile()) {
            return indexFile;
        } else {
            return null;
        }
    }
}
