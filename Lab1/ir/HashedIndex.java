/*  
 *   This file is part of the computer assignment for the
 *   Information Retrieval course at KTH.
 * 
 *   First version:  Johan Boye, 2010
 *   Second version: Johan Boye, 2012
 *   Additions: Hedvig Kjellström, 2012-14
 */



package ir;

import javafx.geometry.Pos;

import java.util.*;


/**
 *   Implements an inverted index as a Hashtable from words to PostingsLists.
 */
public class HashedIndex implements Index {

    /** The index as a hashtable. */
    private HashMap<String,PostingsList> index = new HashMap<String,PostingsList>();


    /**
     *  Inserts this token in the index.
     */
    public void insert( String token, int docID, int offset ) {
	//
	//  YOUR CODE HERE
	//
        if (!index.containsKey(token)) {
            index.put(token, new PostingsList());
        }
        index.get(token).add(docID, offset);

        System.out.println();
    }



    /**
     *  Returns all the words in the index.
     */
    public Iterator<String> getDictionary() {
	//
	//  REPLACE THE STATEMENT BELOW WITH YOUR CODE
	//
        return index.keySet().iterator();

    }


    /**
     *  Returns the postings for a specific term, or null
     *  if the term is not in the index.
     */
    public PostingsList getPostings( String token ) {
	//
	//  REPLACE THE STATEMENT BELOW WITH YOUR CODE
	//
	return index.get(token);
    }


    /**
     *  Searches the index for postings matching the query.
     */
    public PostingsList search( Query query, int queryType, int rankingType, int structureType ) {
	//
	//  REPLACE THE STATEMENT BELOW WITH YOUR CODE
	//
        PostingsList results = null;
        int term_count = query.terms.size();
        for(int i = 0; i < term_count; i++){

        }
        if(queryType == Index.INTERSECTION_QUERY){
            results = intersect(query);
        }
	return results;
    }

    //Finds the intersection (?) of two postinglists.
    public PostingsList intersect(ListIterator<PostingsEntry> p1, ListIterator<PostingsEntry> p2){
        PostingsList result = new PostingsList();
        while(p1.hasNext() && p2.hasNext() ){
            PostingsEntry p1_entry = p1.next();
            PostingsEntry p2_entry = p2.next();
            if(p1_entry.docID == p2_entry.docID){
                result.add(p1_entry);
            }
            //The lists has advanced one step already. Take the one with a bigger ID back one step.
            else if(p1_entry.docID > p2_entry.docID){
                p1.previous();
            }else{
                p2.previous();
            }
        }

        return result;
    }

    public PostingsList intersect(Query query){


        PostingsList result = null;
        int term_count = query.size();

        if(term_count == 0) {
            return null;
        }

        //Adds all postinglists in one list so we can sort it by frequency.
        LinkedList<PostingsList> pListList = new LinkedList<>();
        for(int i = 0; i < term_count; i++){
            pListList.add(getPostings(query.terms.get(i)));
        }
        //sortByFrequency(pListList);

        if(pListList.size() != 0) {
            Iterator<PostingsList> pList = pListList.iterator();
            result = pList.next();
            while (pList.hasNext()) {
                PostingsList tmp = pList.next();
                intersect(result.getIterator(), tmp.getIterator());
            }

        }
        return result;
    }

    //Sorts the list by frequency. Lowerst frequency first.
    public LinkedList<PostingsList> sortByFrequency(LinkedList<PostingsList> list){
        Collections.sort(list, new Comparator<PostingsList>() {
            @Override
            public int compare(PostingsList o1, PostingsList o2) {
                return o1.size()-o2.size();
            }
        });
        return list;
    }



    /**
     *  No need for cleanup in a HashedIndex.
     */
    public void cleanup() {
    }


}
