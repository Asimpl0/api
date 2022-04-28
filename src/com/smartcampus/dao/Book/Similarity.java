package com.smartcampus.dao.Book;

public class Similarity {
    //同当前用户的相似度
        String name;
        Double similarity;

        public Similarity() {
        }

        public Similarity(String name, Double similarity) {
            this.name = name;
            this.similarity = similarity;
        }

}
