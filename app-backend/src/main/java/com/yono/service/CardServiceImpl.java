package com.yono.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yono.dao.CardBenefitRepository;
import com.yono.dao.CardDAO;
import com.yono.dto.CardBenefitDTO;
import com.yono.dto.CardDTO;
import com.yono.entity.CardBenefitEntity;
import com.yono.entity.CardEntity;

@Service
public class CardServiceImpl implements CardService {

    @Autowired
    private CardDAO cardDao;

    @Autowired
    private CardBenefitRepository cardBenefitRepository;

    @Override
    public List<CardDTO> searchCard(String keyword) {
        // Entity -> DTO 변환
        List<CardEntity> entities = cardDao.searchCard(keyword);
        return entities.stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CardDTO createCard(CardDTO cardDTO, List<CardBenefitDTO> benefitsDTOs) {
        if (cardDao.existsByCardTitle(cardDTO.getCardTitle())) {
            throw new IllegalArgumentException("이미 존재하는 카드 이름입니다.");
        }

        CardEntity cardEntity = toEntity(cardDTO);
        cardDao.createCard(cardEntity);

        if (benefitsDTOs != null && !benefitsDTOs.isEmpty()) {
            for (CardBenefitDTO dto : benefitsDTOs) {
                CardBenefitEntity benefitEntity = new CardBenefitEntity();
                benefitEntity.setBenefitTitle(dto.getBenefitTitle());
                benefitEntity.setBenefitValue(dto.getBenefitValue());
                benefitEntity.setBenefitType(dto.getBenefitType());
                benefitEntity.setCardEntity(cardEntity);

                cardBenefitRepository.save(benefitEntity);
            }
        }
        return toDto(cardEntity);
    }

    @Transactional
    @Override
    public void deleteByIds(List<Integer> ids) {
        cardBenefitRepository.deleteByIds(ids);
        cardDao.deleteByIds(ids);
    }

    @Override
    public CardDTO getCardById(Integer id) {
        CardEntity cardEntity = cardDao.findById(id);
        if (cardEntity == null) {
            throw new RuntimeException("Card not found by ID: " + id);
        }
        return toDto(cardEntity);
    }

    // Entity -> DTO 변환
    private CardDTO toDto(CardEntity entity) {
        if (entity == null) {
            return null;
        }

        CardDTO dto = new CardDTO();
        dto.setCardId(entity.getCardId());
        dto.setCardTitle(entity.getCardTitle());
        dto.setCardProvider(entity.getCardProvider());
        dto.setOrganizationCode(entity.getOrganizationCode());

        // , 기준으로 나눈 후 첫 번째 이미지 선택
        String[] images = entity.getCardImgUrl().split(",");
        dto.setCardImgUrl(images.length > 0 ? images[0] : null);

        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());

        return dto;
    }

    // DTO -> Entity 변환
    private CardEntity toEntity(CardDTO dto) {
        CardEntity entity = new CardEntity();
        entity.setCardId(dto.getCardId());
        entity.setCardTitle(dto.getCardTitle());
        entity.setCardProvider(dto.getCardProvider());
        entity.setOrganizationCode(dto.getOrganizationCode());
        entity.setCardImgUrl(dto.getCardImgUrl());
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setUpdatedAt(dto.getUpdatedAt());
        return entity;
    }

}
