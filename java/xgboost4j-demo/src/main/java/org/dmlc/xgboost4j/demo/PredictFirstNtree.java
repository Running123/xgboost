/*
 Copyright (c) 2014 by Contributors 

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at
    
 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */
package org.dmlc.xgboost4j.demo;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.dmlc.xgboost4j.Booster;
import org.dmlc.xgboost4j.DMatrix;
import org.dmlc.xgboost4j.util.Trainer;

import org.dmlc.xgboost4j.demo.util.CustomEval;
import org.dmlc.xgboost4j.demo.util.Params;

/**
 * predict first ntree
 * @author hzx
 */
public class PredictFirstNtree {   
    public static void main(String[] args) {
        // load file from text file, also binary buffer generated by xgboost4j
        DMatrix trainMat = new DMatrix("../../demo/data/agaricus.txt.train");
        DMatrix testMat = new DMatrix("../../demo/data/agaricus.txt.test");
        
        //specify parameters
        Params param = new Params() {
            {
                put("eta", 1.0);
                put("max_depth", 2);
                put("silent", 1);
                put("objective", "binary:logistic");
            }
        };
        
        //specify watchList
        List<Map.Entry<String, DMatrix>> watchs =  new ArrayList<>();
        watchs.add(new AbstractMap.SimpleEntry<>("train", trainMat));
        watchs.add(new AbstractMap.SimpleEntry<>("test", testMat));
        
        //train a booster
        int round = 3;
        Booster booster = Trainer.train(param, trainMat, round, watchs, null, null);
        
        //predict use 1 tree
        float[][] predicts1 = booster.predict(testMat, false, 1);
        //by default all trees are used to do predict
        float[][] predicts2 = booster.predict(testMat);
        
        //use a simple evaluation class to check error result
        CustomEval eval = new CustomEval();
        System.out.println("error of predicts1: " + eval.eval(predicts1, testMat));
        System.out.println("error of predicts2: " + eval.eval(predicts2, testMat));
    }
}
