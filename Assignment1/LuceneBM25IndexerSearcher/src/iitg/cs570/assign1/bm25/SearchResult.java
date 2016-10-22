/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iitg.cs570.assign1.bm25;

/**
 *
 * @author Neelesh
 */
public class SearchResult implements Comparable<SearchResult>{
    
    private String title;
    private String path;
    private float score;
    private String summary;

    
    public SearchResult(String title, String fileName, float score, String summary)
    {
        this.title=title;
        this.path= formRelativePath(fileName);
        this.score= score;
        this.summary= summary;
    }
    
    private static String formRelativePath(String fileName)
    {
    return LuceneConstants.PATH_PREFIX+fileName;
    }
    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }
    
    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
    
    public int compareTo(SearchResult compareResult) {

		float compareScore = ((SearchResult) compareResult).getScore();

	if (this.score < compareScore) return -1;
        if (this.score > compareScore) return 1;
        return 0;


	}
   
    
}
