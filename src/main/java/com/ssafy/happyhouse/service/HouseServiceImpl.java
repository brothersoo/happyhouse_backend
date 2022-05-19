package com.ssafy.happyhouse.service;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xml.sax.SAXException;

import com.ssafy.happyhouse.model.house.DealInfo;
import com.ssafy.happyhouse.model.house.HouseDeal;
import com.ssafy.happyhouse.model.house.HouseInfo;
import com.ssafy.happyhouse.repository.HouseRepository;
import com.ssafy.happyhouse.util.xmlparser.HouseDealAPIHandler;

@Service
public class HouseServiceImpl implements HouseService {
	
	private HouseRepository houseRepository;
	
	private HouseDealAPIHandler houseDealAPIHandler = HouseDealAPIHandler.getInstance();

	public HouseServiceImpl(HouseRepository houseRepository) {
		this.houseRepository = houseRepository;
	}

	@Override
	public List<DealInfo> getDealsByCodeDate(String code, int dealYear, int dealMonth, String standard) {
		List<DealInfo> dealList = houseRepository.findByAreaCodeDate(code, dealYear, dealMonth);
		
		if (standard.equals("aptName")) {
			Collections.sort(dealList, (deal1, deal2) -> {
				return deal1.getHouseInfo().getAptName().compareTo(deal2.getHouseInfo().getAptName());
			});
		} else if (standard.equals("dealDate")) {
			Collections.sort(dealList, (deal1, deal2) -> {
				return deal1.getDealDay() - deal2.getDealDay();
			});
		} else if (standard.equals("-dealDate")) {
			Collections.sort(dealList, (deal1, deal2) -> {
				return deal2.getDealDay() - deal1.getDealDay();
			});
		}
		return dealList;
	}

	@Override
	@Transactional
	public int updateDeal(String code, int year, int month) throws IOException, ParserConfigurationException, SAXException {
		List<HouseDeal> houseDealsFromOpenAPI = houseDealAPIHandler.getMonthlyAreaHouseDeal(code, year, month);
		List<DealInfo> storedHouseDeals = houseRepository.findByAreaCodeDate(code, year, month);

		Set<HouseInfo> storedHouseInfos = new HashSet<>();
		for (HouseInfo houseInfo : houseRepository.findAllHouses()) {
			storedHouseInfos.add(houseInfo);
		}
		Set<DealInfo> storedDealInfos = new HashSet<>();

		for (DealInfo houseDeal : storedHouseDeals) {
			storedDealInfos.add(houseDeal);
		}

		Set<HouseInfo> newHouses = new HashSet<>();
		Set<DealInfo> newDeals = new HashSet<>();
		for (HouseDeal houseDeal : houseDealsFromOpenAPI) {
			if (!storedHouseInfos.contains(houseDeal.getHouseInfo()) ) {
				newHouses.add(houseDeal.getHouseInfo());
			}
			if (!storedDealInfos.contains(houseDeal.getDealInfo())) {
				newDeals.add(houseDeal.getDealInfo());
			}
		}

		if (!newHouses.isEmpty()) {
			houseRepository.insertBatchHouse(newHouses);
		}
		if (!newDeals.isEmpty()) {
			houseRepository.insertBatchDeal(newDeals);
		}
		return newDeals.size();
	}
}
