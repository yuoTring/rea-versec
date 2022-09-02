package com.versec.versecko.data.mapper

import com.versec.domain.model.UserModel
import com.versec.versecko.data.entity.UserEntity

class UserMapper {
    fun entityToModel(userEntity: UserEntity) : UserModel{

        return UserModel(
            uid = userEntity.uid,
            nickName = userEntity.nickName,
            gender = userEntity.gender,
            age = userEntity.age,
            birth = userEntity.birth,

            mainResidence = userEntity.mainResidence,
            subResidence = userEntity.subResidence,
            tripWish = userEntity.tripWish,
            tripStyle = userEntity.tripStyle,
            selfIntroduction = userEntity.selfIntroduction,

            uriList = userEntity.uriList,
            geohash = userEntity.geohash,
            latitude = userEntity.latitude,
            longitude = userEntity.longitude,

            mannerScore = userEntity.mannerScore,
            premiumOrNot = userEntity.premiumOrNot,
            knock = userEntity.knock
        )
    }

    fun modelToEntity (userModel: UserModel) : UserEntity {

        return UserEntity(
            uid = userModel.uid,
            nickName = userModel.nickName,
            gender = userModel.gender,
            age = userModel.age,
            birth = userModel.birth,

            mainResidence = userModel.mainResidence,
            subResidence = userModel.subResidence,
            tripWish = userModel.tripWish,
            tripStyle = userModel.tripStyle,
            selfIntroduction = userModel.selfIntroduction,

            uriList = userModel.uriList,
            geohash = userModel.geohash,
            latitude = userModel.latitude,
            longitude = userModel.longitude,

            mannerScore = userModel.mannerScore,
            premiumOrNot = userModel.premiumOrNot,
            knock = userModel.knock
        )
    }
}