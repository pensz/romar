/**
 * Copyright 2012 Anjuke Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.anjuke.romar.mahout.factory;

import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.CachingItemSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.apache.mahout.common.ClassUtils;

import com.anjuke.romar.core.RomarConfig;
import com.anjuke.romar.mahout.GenericReloadDataModel;
import com.anjuke.romar.mahout.MahoutService;
import com.anjuke.romar.mahout.RecommenderWrapper;

public class MahoutServiceItemRecommendFactory extends
        AbstractMahoutServiceFactory implements MahoutServiceFactory {

    @Override
    public MahoutService createService() {
        RomarConfig config = RomarConfig.getInstance();
        Recommender recommender;
        DataModel dataModel = wrapDataModel(new GenericReloadDataModel());

        ItemSimilarity similarity = ClassUtils.instantiateAs(
                config.getItemSimilarityClass(), ItemSimilarity.class,
                new Class<?>[] {DataModel.class}, new Object[] {dataModel});
        if (config.isUseSimilariyCache()) {
            similarity = new CachingItemSimilarity(similarity,
                    config.getSimilarityCacheSize());
        }
        recommender = new GenericItemBasedRecommender(dataModel, similarity);
        return new RecommenderWrapper(recommender);
    }

}
