package com.kakaofriends_mockup_project.api;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import com.kakaofriends_mockup_project.utils.CacheUtils;
import com.kakaofriends_mockup_project.utils.CacheValue;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api")
@RestController
public class ApiController {

    // TODO: key를 값 그대로 박는게 아니라 동적으로 매핑 받을 수 있으면 좋을 듯...
    // TODO 캐시키를 동적으로 만들도록 고도화 - (key = "t_isLogin", ttl = "10")
    @ApiCached(key = "1596428228035")
    @GetMapping("/category/items")
    public ResponseEntity<String> getCategoryItems(@RequestParam("t") String param) throws Exception {

        // TODO AOP 내부로 이동 - setCache
        setCache(param, getCategoryItemsJson());                        // 일단 parameter를 key로 가정

        // TODO AOP 내부로 이동 - getDataOfCacheValueByKey
        return ResponseEntity.ok((String) getDataOfCacheValueByKey(param));
    }

    @ApiCached(key = "1596515219687")
    @GetMapping("/home")
    public ResponseEntity<String> getHome(@RequestParam Map<String, Object> param) throws Exception {
        String key = (String) param.get("t");

        setCache(key, getHomeJson());

        return ResponseEntity.ok((String) getDataOfCacheValueByKey(key));
    }

    @ApiCached(key = "1596515219688")
    @GetMapping("/members/basket")
    public ResponseEntity<String> getMembersBasket(@RequestParam("t") String param) throws Exception {

        setCache(param, getMembersBasketJson());

        return ResponseEntity.ok((String) getDataOfCacheValueByKey(param));
    }

    // FIXME 무조건 SET을 하고 있음 - 필요할 때만 DATA READ 하도록 수정
    // TODO 컨트롤러가 아닌 AOP 내부로직으로 이동 
    public void setCache(String key, String value) {

        CacheUtils.cacheMap.set(key, getCacheValue(value));
    }

    public CacheValue getCacheValue(String value) {

        // TODO createdTime는 데이터와 묶이지 않도록 분리
        Map<String, Object> map = new HashMap<>();
        // TODO 직렬화/역직렬화 대응 (애초에 String으로 떨어지게 로직 자체를 변경) -> 클래스가 지원하는 직렬화/역직렬화를 믿는 게 맞는 방법일 수도 있음
        map.put("createdTime", LocalDateTime.now());
        map.put("data", value);

        String test = "Test";
        test = test + "1";
        
        // TODO 빌더를 쓰는 이유는 데이터 임뮤터블 보장 -> 지금은 보장해주지 못하고 있기 때문에 -> 빌더를 걷어내든가, 아니면 내부 데이터를 임뮤터블하게 쓰든가
        return CacheValue.builder()
                .map(map)
                .build();
    }

    public Object getDataOfCacheValueByKey(String key) {

        CacheValue cacheValue = (CacheValue) CacheUtils.cacheMap.getValue(key);

        return cacheValue.getMap().get("data"); // FIXME 필드명이 하드코딩 되어 있음 - 추적을 할 수가 없다 -> 지양해야 함
    }

    public String getCategoryItemsJson() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "[{\"categorySeq\":1,\"code\":1,\"name\":\"카테고리\",\"parentCode\":0,\"step\":1}," +
                "{\"categorySeq\":2,\"code\":2,\"name\":\"카카오프렌즈\",\"parentCode\":1,\"image1\":\"category_all_W_200428.jpg\",\"image2\":\"category_all_M_200428.jpg\",\"step\":1}," +
                "{\"categorySeq\":3,\"code\":3,\"name\":\"카테고리\",\"parentCode\":1,\"image1\":\"category_all_W_200428.jpg\",\"image2\":\"category_all_M_200428.jpg\",\"step\":2}," +
                "{\"categorySeq\":4,\"code\":4,\"name\":\"캐릭터\",\"parentCode\":2,\"image1\":\"category_all_W_200428.jpg\",\"image2\":\"category_all_M_200428.jpg\",\"step\":1}," +
                "{\"categorySeq\":103,\"code\":103,\"name\":\"테마 기획전\",\"parentCode\":3,\"image1\":\"190916_theme_all_W.jpg\",\"image2\":\"190916_theme_all_M.jpg\",\"step\":1}," +
                "{\"categorySeq\":64,\"code\":64,\"name\":\"토이\",\"parentCode\":3,\"image1\":\"category_toy_W.jpg\",\"image2\":\"category_toy_M.jpg\",\"step\":2}," +
                "{\"categorySeq\":22,\"code\":22,\"name\":\"리빙\",\"parentCode\":3,\"image1\":\"category_living_W.jpg\",\"image2\":\"category_living_M.jpg\",\"step\":4}," +
                "{\"categorySeq\":13,\"code\":13,\"name\":\"잡화\",\"parentCode\":3,\"image1\":\"category_accessory_W.jpg\",\"image2\":\"category_accessory_M.jpg\",\"step\":5}," +
                "{\"categorySeq\":15,\"code\":15,\"name\":\"문구\",\"parentCode\":3,\"image1\":\"category_stationery_W_200428.jpg\",\"image2\":\"category_stationery_M_200428.jpg\",\"step\":6}," +
                "{\"categorySeq\":14,\"code\":14,\"name\":\"의류\",\"parentCode\":3,\"image1\":\"category_apparel_W_200428.jpg\",\"image2\":\"category_apparel_M_200428.jpg\",\"step\":7}," +
                "{\"categorySeq\":140,\"code\":140,\"name\":\"파자마\",\"parentCode\":3,\"image1\":\"category_pajamas_W_200428.jpg\",\"image2\":\"category_pajamas_M_200428.jpg\",\"step\":8}," +
                "{\"categorySeq\":17,\"code\":17,\"name\":\"여행/레져\",\"parentCode\":3,\"image1\":\"category_leisure_W.jpg\",\"image2\":\"category_leisure_M.jpg\",\"step\":9}," +
                "{\"categorySeq\":16,\"code\":16,\"name\":\"생활테크\",\"parentCode\":3,\"image1\":\"category_electronic_W_200428.jpg\",\"image2\":\"category_electronic_M_200428.jpg\",\"step\":10}," +
                "{\"categorySeq\":138,\"code\":138,\"name\":\"폰 액세서리\",\"parentCode\":3,\"image1\":\"category_mobileaccessory_W_200428.jpg\",\"image2\":\"category_mobileaccessory_M_200428.jpg\",\"step\":11}," +
                "{\"categorySeq\":25,\"code\":25,\"name\":\"식품\",\"parentCode\":3,\"image1\":\"category_food_W.jpg\",\"image2\":\"category_food_M.jpg\",\"step\":12}," +
                "{\"categorySeq\":115,\"code\":115,\"name\":\"니니즈\",\"parentCode\":3,\"image1\":\"category_niniz_W_200527.jpg\",\"image2\":\"category_niniz_M_200527.jpg\",\"image3\":\"gnb_niniz.png\",\"image5\":\"gnb_niniz_off.png\",\"image6\":\"gnb_niniz_on.png\",\"step\":16}," +
                "{\"categorySeq\":132,\"code\":132,\"name\":\"선데이치즈볼\",\"parentCode\":3,\"image1\":\"190910_W_categorytop_sundaycheezzzball_f0321e_main_1080x220.jpg\",\"image2\":\"190910_M_categorytop_sundaycheezzzball_f0321e_main_750x260.jpg\",\"image3\":\"gnb-cheezz.png\",\"image5\":\"gnb_sunday_cheezzzball_off.png\",\"image6\":\"gnb_sunday_cheezzzball_on.png\",\"step\":17}," +
                "{\"categorySeq\":23,\"code\":23,\"name\":\"라이언\",\"parentCode\":4,\"titleImage\":\"ryan0.png\",\"image1\":\"190909_character_ryan_W.jpg\",\"image2\":\"190909_character_ryan_M.jpg\",\"image3\":\"search-ryan.png\",\"image5\":\"category_rion_off.png\",\"image6\":\"category_rion_on.png\",\"step\":1}," +
                "{\"categorySeq\":6,\"code\":6,\"name\":\"어피치\",\"parentCode\":4,\"image1\":\"190909_character_apeach_W.jpg\",\"image2\":\"190909_character_apeach_M.jpg\",\"image3\":\"search-apeach.png\",\"image5\":\"category_apeach_off.png\",\"image6\":\"category_apeach_on.png\",\"step\":2}," +
                "{\"categorySeq\":5,\"code\":5,\"name\":\"무지\",\"parentCode\":4,\"titleImage\":\"muji0.png\",\"image1\":\"190909_character_muzi_W.jpg\",\"image2\":\"190909_character_muzi_M.jpg\",\"image3\":\"search-muzi.png\",\"image5\":\"category_muzi_off.png\",\"image6\":\"category_muzi_on.png\",\"step\":3}," +
                "{\"categorySeq\":7,\"code\":7,\"name\":\"프로도\",\"parentCode\":4,\"image1\":\"190909_character_frodo_W.jpg\",\"image2\":\"190909_character_frodo_M.jpg\",\"image3\":\"search-frodo.png\",\"image5\":\"category_frodo_off.png\",\"image6\":\"category_frodo_on.png\",\"step\":4}," +
                "{\"categorySeq\":8,\"code\":8,\"name\":\"네오\",\"parentCode\":4,\"image1\":\"190909_character_neo_W.jpg\",\"image2\":\"190909_character_neo_M.jpg\",\"image3\":\"search-neo.png\",\"image5\":\"category_neo_off.png\",\"image6\":\"category_neo_on.png\",\"step\":5}," +
                "{\"categorySeq\":9,\"code\":9,\"name\":\"튜브\",\"parentCode\":4,\"image1\":\"190909_character_tube_W.jpg\",\"image2\":\"190909_character_tube_M.jpg\",\"image3\":\"search-tube.png\",\"image5\":\"category_tube_off.png\",\"image6\":\"category_tube_on.png\",\"step\":6}," +
                "{\"categorySeq\":10,\"code\":10,\"name\":\"제이지\",\"parentCode\":4,\"image1\":\"190909_character_jay-z_W.jpg\",\"image2\":\"190909_character_jay-z_M.jpg\",\"image3\":\"search-jayg.png\",\"image5\":\"category_jayz_off.png\",\"image6\":\"category_jayz_on.png\",\"step\":7}," +
                "{\"categorySeq\":11,\"code\":11,\"name\":\"콘\",\"parentCode\":4,\"titleImage\":\"Koala.jpg\",\"image1\":\"190909_character_con_W.jpg\",\"image2\":\"190909_character_con_M.jpg\",\"image3\":\"search-con.png\",\"image5\":\"category_con_off.png\",\"image6\":\"category_con_on.png\",\"step\":8}," +
                "{\"categorySeq\":27,\"code\":27,\"name\":\"피규어\",\"parentCode\":12,\"image1\":\"W021_toy_figure.jpg\",\"image2\":\"M021_toy_figure.jpg\",\"step\":1}," +
                "{\"categorySeq\":28,\"code\":28,\"name\":\"브릭\",\"parentCode\":12,\"image1\":\"W022_toy_brick_modify.jpg\",\"image2\":\"M022_toy_brick.jpg\",\"step\":2}," +
                "{\"categorySeq\":29,\"code\":29,\"name\":\"퍼즐\",\"parentCode\":12,\"image1\":\"W023_toy_puzzle.jpg\",\"image2\":\"M023_toy_puzzle.jpg\",\"step\":3}," +
                "{\"categorySeq\":31,\"code\":31,\"name\":\"신발\",\"parentCode\":13,\"image1\":\"W041_accessory_shoes.jpg\",\"image2\":\"M041_accessory_shoes.jpg\",\"step\":1}," +
                "{\"categorySeq\":30,\"code\":30,\"name\":\"파우치/지갑/가방\",\"parentCode\":13,\"image1\":\"W042_accessory_pouch.jpg\",\"image2\":\"M042_accessory_pouch.jpg\",\"step\":2}," +
                "{\"categorySeq\":34,\"code\":34,\"name\":\"패션소품\",\"parentCode\":13,\"image1\":\"W043_accessory_fashion.jpg\",\"image2\":\"M043_accessory_fashion.jpg\",\"step\":3}," +
                "{\"categorySeq\":33,\"code\":33,\"name\":\"우산\",\"parentCode\":13,\"image1\":\"W044_accessory_umbrella.jpg\",\"image2\":\"M044_accessory_umbrella.jpg\",\"step\":4}," +
                "{\"categorySeq\":83,\"code\":83,\"name\":\"시즌잡화\",\"parentCode\":13,\"image1\":\"W047_accessory_season.jpg\",\"image2\":\"M047_accessory_season.jpg\",\"step\":7}," +
                "{\"categorySeq\":79,\"code\":79,\"name\":\"여성\",\"parentCode\":14,\"image1\":\"W051_fashion_women.jpg\",\"image2\":\"M051_fashion_women.jpg\",\"step\":1}," +
                "{\"categorySeq\":78,\"code\":78,\"name\":\"남성\",\"parentCode\":14,\"image1\":\"W052_fashion_men.jpg\",\"image2\":\"M052_fashion_men.jpg\",\"step\":2}," +
                "{\"categorySeq\":37,\"code\":37,\"name\":\"속옷\",\"parentCode\":14,\"image1\":\"W056_fashion_under.jpg\",\"image2\":\"M056_fashion_under.jpg\",\"step\":5}," +
                "{\"categorySeq\":80,\"code\":80,\"name\":\"양말\",\"parentCode\":14,\"image1\":\"W055_fashion_socks.jpg\",\"image2\":\"M055_fashion_socks.jpg\",\"step\":6}," +
                "{\"categorySeq\":44,\"code\":44,\"name\":\"필기구\",\"parentCode\":15,\"image1\":\"W072_stationery_pen.jpg\",\"image2\":\"M072_stationery_pen.jpg\",\"step\":1}," +
                "{\"categorySeq\":45,\"code\":45,\"name\":\"필통/케이스\",\"parentCode\":15,\"image1\":\"W073_stationery_case.jpg\",\"image2\":\"M073_stationery_case.jpg\",\"step\":2}," +
                "{\"categorySeq\":46,\"code\":46,\"name\":\"노트/메모\",\"parentCode\":15,\"image1\":\"W074_stationery_note.jpg\",\"image2\":\"M074_stationery_note.jpg\",\"step\":3}," +
                "{\"categorySeq\":81,\"code\":81,\"name\":\"파일\",\"parentCode\":15,\"image1\":\"W076_stationery_file.jpg\",\"image2\":\"M076_stationery_file.jpg\",\"step\":4}," +
                "{\"categorySeq\":43,\"code\":43,\"name\":\"스티커\",\"parentCode\":15,\"image1\":\"W071_stationery_sticker.jpg\",\"image2\":\"M071_stationery_sticker.jpg\",\"step\":6}," +
                "{\"categorySeq\":89,\"code\":89,\"name\":\"데스크 소품\",\"parentCode\":15,\"image1\":\"W078_stationery_desk.jpg\",\"image2\":\"M078_stationery_desk.jpg\",\"step\":7}," +
                "{\"categorySeq\":47,\"code\":47,\"name\":\"카드/엽서\",\"parentCode\":15,\"image1\":\"W075_stationery_card.jpg\",\"image2\":\"M075_stationery_card.jpg\",\"step\":8}," +
                "{\"categorySeq\":92,\"code\":92,\"name\":\"선물 포장\",\"parentCode\":15,\"image1\":\"W079_stationery_giftbag.jpg\",\"image2\":\"M079_stationery_giftbag.jpg\",\"step\":10}," +
                "{\"categorySeq\":56,\"code\":56,\"name\":\"노트북 액세서리\",\"parentCode\":16,\"image1\":\"W093_tech_laptop.png\",\"image2\":\"M093_tech_laptop.png\",\"step\":3}," +
                "{\"categorySeq\":58,\"code\":58,\"name\":\"소형 전자\",\"parentCode\":16,\"image1\":\"W034_living_lighting.jpg\",\"image2\":\"M034_living_lighting.jpg\",\"step\":5}," +
                "{\"categorySeq\":148,\"code\":148,\"name\":\"에어팟 케이스\",\"parentCode\":16,\"step\":6}," +
                "{\"categorySeq\":149,\"code\":149,\"name\":\"에어팟 액세서리\",\"parentCode\":16,\"step\":7}," +
                "{\"categorySeq\":53,\"code\":53,\"name\":\"여행\",\"parentCode\":17,\"image1\":\"W081_leisure_travel.png\",\"image2\":\"M081_leisure_travel.png\",\"step\":1}," +
                "{\"categorySeq\":50,\"code\":50,\"name\":\"레져\",\"parentCode\":17,\"image1\":\"W082_leisure_golf_modify.png\",\"image2\":\"M082_leisure_golf.png\",\"step\":2}," +
                "{\"categorySeq\":70,\"code\":70,\"name\":\"쿠션/방석\",\"parentCode\":22,\"image1\":\"W032_living_fabric.jpg\",\"image2\":\"M032_living_fabric.jpg\",\"step\":1}," +
                "{\"categorySeq\":69,\"code\":69,\"name\":\"컵/텀블러\",\"parentCode\":22,\"image1\":\"W031_living_cup.jpg\",\"image2\":\"M031_living_cup.jpg\",\"step\":2}," +
                "{\"categorySeq\":74,\"code\":74,\"name\":\"주방용품\",\"parentCode\":22,\"image1\":\"W035_living_kitchen.jpg\",\"image2\":\"M035_living_kitchen.jpg\",\"step\":5}," +
                "{\"categorySeq\":75,\"code\":75,\"name\":\"미용/욕실용품\",\"parentCode\":22,\"image1\":\"W036_living_bathroom.jpg\",\"image2\":\"M036_living_bathroom.jpg\",\"step\":6}," +
                "{\"categorySeq\":76,\"code\":76,\"name\":\"생활소품/잡화\",\"parentCode\":22,\"image1\":\"W037_living_accessory_modify.jpg\",\"image2\":\"M037_living_accessory.jpg\",\"step\":8}," +
                "{\"categorySeq\":136,\"code\":136,\"name\":\"펫 용품\",\"parentCode\":22,\"image1\":\"190727_category_pet_W_FFCF68.jpg\",\"image2\":\"190727_category_pet_M_FFCF68.jpg\",\"step\":9}," +
                "{\"categorySeq\":72,\"code\":72,\"name\":\"탈취/방향제\",\"parentCode\":22,\"image1\":\"W033_living_airfresh_modify.jpg\",\"image2\":\"M033_living_airfresh.jpg\",\"step\":11}," +
                "{\"categorySeq\":38,\"code\":38,\"name\":\"귀걸이\",\"parentCode\":24,\"image1\":\"W061_jewelry_earring0.png\",\"image2\":\"M061_jewelry_earring0.png\",\"step\":1}," +
                "{\"categorySeq\":39,\"code\":39,\"name\":\"반지\",\"parentCode\":24,\"image1\":\"W062_jewelry_ring0.png\",\"image2\":\"M062_jewelry_ring0.png\",\"step\":2}," +
                "{\"categorySeq\":84,\"code\":84,\"name\":\"가방참/핀뱃지\",\"parentCode\":24,\"image1\":\"W065_jewelry_charm0.png\",\"image2\":\"M065_jewelry_charm0.png\",\"step\":5}," +
                "{\"categorySeq\":102,\"code\":102,\"name\":\"헤어스트링\",\"parentCode\":24,\"image1\":\"W151_hairstring_all.png\",\"image2\":\"M151_hairstring_all.png\",\"step\":7}," +
                "{\"categorySeq\":63,\"code\":63,\"name\":\"스낵\",\"parentCode\":25,\"image1\":\"W111_food_snack.jpg\",\"image2\":\"M111_food_snack.jpg\",\"step\":1}," +
                "{\"categorySeq\":86,\"code\":86,\"name\":\"음료\",\"parentCode\":25,\"image1\":\"W112_food_tea.jpg\",\"image2\":\"M112_food_tea.jpg\",\"step\":2}," +
                "{\"categorySeq\":60,\"code\":60,\"name\":\"식기\",\"parentCode\":26,\"image1\":\"W101_kid_table.png\",\"image2\":\"M101_kid_table.png\",\"step\":1}," +
                "{\"categorySeq\":61,\"code\":61,\"name\":\"잡화\",\"parentCode\":26,\"image1\":\"W102_kid_acc.png\",\"image2\":\"M102_kid_acc.png\",\"step\":2}," +
                "{\"categorySeq\":65,\"code\":65,\"name\":\"미니인형\",\"parentCode\":64,\"image1\":\"W011_plush_mini_modify.jpg\",\"image2\":\"M011_plush_mini.jpg\",\"step\":1}," +
                "{\"categorySeq\":66,\"code\":66,\"name\":\"중형인형\",\"parentCode\":64,\"image1\":\"W012_plush_35cm_modify.jpg\",\"image2\":\"M012_plush_35cm.jpg\",\"step\":2}," +
                "{\"categorySeq\":67,\"code\":67,\"name\":\"대형인형\",\"parentCode\":64,\"image1\":\"W013_plush_giant.jpg\",\"image2\":\"M013_plush_giant.jpg\",\"step\":3}," +
                "{\"categorySeq\":68,\"code\":68,\"name\":\"키체인인형\",\"parentCode\":64,\"image1\":\"W014_plush_keychain.jpg\",\"image2\":\"M014_plush_keychain.jpg\",\"step\":4}," +
                "{\"categorySeq\":94,\"code\":94,\"name\":\"피규어/브릭\",\"parentCode\":64,\"image1\":\"W021_toy_figure.jpg\",\"image2\":\"M021_toy_figure.jpg\",\"step\":5}," +
                "{\"categorySeq\":88,\"code\":88,\"name\":\"크리스마스\",\"parentCode\":87,\"image1\":\"W200_event_christmas0.jpg\",\"image2\":\"M200_event_christmas0.jpg\",\"step\":1}," +
                "{\"categorySeq\":91,\"code\":91,\"name\":\"뮤지엄\",\"parentCode\":90,\"image1\":\"W121_museum_frame.jpg\",\"image2\":\"M121_museum_frame.jpg\",\"step\":1}," +
                "{\"categorySeq\":96,\"code\":96,\"name\":\"이지웨어\",\"parentCode\":95,\"image1\":\"W131_omf_easywear.png\",\"image2\":\"M132_omf_easywear.png\",\"step\":1}," +
                "{\"categorySeq\":97,\"code\":97,\"name\":\"액티브웨어\",\"parentCode\":95,\"image1\":\"W132_omf_activewear.png\",\"image2\":\"M132_omf_activewear.png\",\"step\":2}," +
                "{\"categorySeq\":99,\"code\":99,\"name\":\"악세서리\",\"parentCode\":95,\"image1\":\"W132_omf_accessory.png\",\"image2\":\"M132_omf_accessory.png\",\"step\":4}," +
                "{\"categorySeq\":101,\"code\":101,\"name\":\"수비니어\",\"parentCode\":100,\"image1\":\"W131_souvenir_all.png\",\"image2\":\"M131_souvenir_all.png\",\"step\":1}," +
                "{\"categorySeq\":163,\"code\":163,\"name\":\"아크메드라비\",\"parentCode\":103,\"image1\":\"200730_category_ADLV_W.jpg\",\"image2\":\"200730_category_ADLV_M.jpg\",\"step\":23}," +
                "{\"categorySeq\":162,\"code\":162,\"name\":\"비치펍\",\"parentCode\":103,\"image1\":\"200701_category_beachpub_W.jpg\",\"image2\":\"200701_category_beachpub_M.jpg\",\"step\":24}," +
                "{\"categorySeq\":161,\"code\":161,\"name\":\"마린 블루\",\"parentCode\":103,\"image1\":\"200604_category_marineblue_W.jpg\",\"image2\":\"200604_category_marineblue_M.jpg\",\"step\":25}," +
                "{\"categorySeq\":160,\"code\":160,\"name\":\"얌얌프렌즈\",\"parentCode\":103,\"image1\":\"200504_category_yumyum_W.jpg\",\"image2\":\"200504_category_yumyum_M.jpg\",\"step\":26}," +
                "{\"categorySeq\":159,\"code\":159,\"name\":\"레몬테라스\",\"parentCode\":103,\"image1\":\"200427_category_lemonterrace_W.jpg\",\"image2\":\"200427_category_lemonterrace_M.jpg\",\"step\":27}," +
                "{\"categorySeq\":158,\"code\":158,\"name\":\"베이비드리밍\",\"parentCode\":103,\"image1\":\"200310_category_babydreaming_W.jpg\",\"image2\":\"200310_category_babydreaming_M.jpg\",\"step\":28}," +
                "{\"categorySeq\":157,\"code\":157,\"name\":\"해피위크\",\"parentCode\":103,\"image1\":\"200218_category_happweeks_W.jpg\",\"image2\":\"200218_category_happweeks_M.jpg\",\"step\":29}," +
                "{\"categorySeq\":156,\"code\":156,\"name\":\"강다니엘 에디션\",\"parentCode\":103,\"image1\":\"200206_category_DANIEL_W.jpg\",\"image2\":\"200206_category_DANIEL_M.jpg\",\"step\":30}," +
                "{\"categorySeq\":155,\"code\":155,\"name\":\"치즈프렌즈\",\"parentCode\":103,\"image1\":\"200102_theme_cheese_W.jpg\",\"image2\":\"200102_theme_cheese_M.jpg\",\"step\":31}," +
                "{\"categorySeq\":152,\"code\":152,\"name\":\"트와이스 에디션\",\"parentCode\":103,\"image1\":\"191017_theme_twice_W.jpg\",\"image2\":\"191017_theme_twice_M.jpg\",\"step\":33}," +
                "{\"categorySeq\":144,\"code\":144,\"name\":\"포레스트 라이언\",\"parentCode\":103,\"image1\":\"200521_category_forest2_W.jpg\",\"image2\":\"200521_category_forest2_M.jpg\",\"image3\":\"GNB_forest_W_2.png\",\"step\":35}," +
                "{\"categorySeq\":126,\"code\":126,\"name\":\"러블리 어피치\",\"parentCode\":103,\"image1\":\"190916_theme_Lovelyapeach_W.jpg\",\"image2\":\"190916_theme_Lovelyapeach_M.jpg\",\"image3\":\"190221_gnb_lovelyapeach_W.png\",\"image4\":\"190221_gnb_lovelyapeach_M.png\",\"step\":39}," +
                "{\"categorySeq\":116,\"code\":116,\"name\":\"케로&베로니\",\"parentCode\":115,\"image1\":\"20180816_W_category_keroberony_fad7dc.jpg\",\"image2\":\"20180816_M_category_keroberony_fad7dc.jpg\",\"image3\":\"20180816_W_theme_GNB_keroberony_2x.png\",\"image4\":\"20180816_M_theme_GNB_keroberony.png\",\"step\":1}," +
                "{\"categorySeq\":117,\"code\":117,\"name\":\"앙몬드\",\"parentCode\":115,\"image1\":\"20180928_W_categorytop_angmond_4360ab.jpg\",\"image2\":\"20180928_M_categorytop_angmond_4360ab.jpg\",\"image3\":\"20181002_angmond_GNB_W@2x.png\",\"image4\":\"20181002_angmond_GNB_M.png\",\"step\":2}," +
                "{\"categorySeq\":118,\"code\":118,\"name\":\"스카피\",\"parentCode\":115,\"image1\":\"20180928_W_categorytop_scappy_ec6d6d.jpg\",\"image2\":\"20180928_M_categorytop_scappy_ec6d6d.jpg\",\"image3\":\"20181002_scappy_GNB_W@2x.png\",\"image4\":\"20181002_scappy_GNB_M.png\",\"step\":3}," +
                "{\"categorySeq\":131,\"code\":131,\"name\":\"죠르디\",\"parentCode\":115,\"image1\":\"190507_categorybanner_jordy_W_A0E6AE.jpg\",\"image2\":\"190507_categorybanner_jordy_M_A0E6AE.jpg\",\"image3\":\"190527_GNBbanner_Jordy_W.png\",\"step\":4}," +
                "{\"categorySeq\":153,\"code\":153,\"name\":\"팬다주니어\",\"parentCode\":115,\"step\":5}," +
                "{\"categorySeq\":134,\"code\":134,\"name\":\"굿즈\",\"parentCode\":132,\"image1\":\"190614_W_categorytop_sundaycheezzzball_sub_goods_f0321e.jpg\",\"image2\":\"190614_M_categorytop_sundaycheezzzball_f0321e-04.jpg\",\"step\":1}," +
                "{\"categorySeq\":135,\"code\":135,\"name\":\"스낵\",\"parentCode\":132,\"image1\":\"190614_W_categorytop_sundaycheezzzball_sub_snack_f0321e.jpg\",\"image2\":\"190614_M_categorytop_sundaycheezzzball_f0321e-02.jpg\",\"step\":2}," +
                "{\"categorySeq\":145,\"code\":145,\"name\":\"휴대폰 케이스\",\"parentCode\":138,\"step\":1}," +
                "{\"categorySeq\":146,\"code\":146,\"name\":\"휴대폰 액세서리\",\"parentCode\":138,\"step\":2}," +
                "{\"categorySeq\":147,\"code\":147,\"name\":\"충전기\",\"parentCode\":138,\"step\":3}," +
                "{\"categorySeq\":141,\"code\":141,\"name\":\"여성\",\"parentCode\":140,\"step\":1}," +
                "{\"categorySeq\":142,\"code\":142,\"name\":\"남성\",\"parentCode\":140,\"step\":2}," +
                "{\"categorySeq\":143,\"code\":143,\"name\":\"키즈\",\"parentCode\":140,\"step\":3}]";
    }

    public String getHomeJson() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "{\"totalCount\":53,\"totalPageCount\":6,\"page\":1," +
                "\"resultList\":[" +
                "{\"homeSeq\":278,\"badgeSeq\":0,\"type\":\"A\",\"mainImage\":\"https://t1.kakaocdn.net/friends/prod/main_tab/home/home_20200722153145_kr.jpg\",\"badgeName\":\"NEW\",\"badgeColor\":\"#3ed2e5\",\"contentsType\":\"I\",\"title\":\"이렇게 완벽한 탁상시계\",\"linkUrl\":\"https://store.kakaofriends.com/kr/promotions/promotion/433\",\"credate\":\"2020-07-21 15:09:19.125\",\"moddate\":\"2020-07-30 11:59:35.026\",\"creuser\":\"leah.koh\",\"moduser\":\"sam.hj\",\"dbsts\":\"A\",\"language\":\"kr\",\"displays\":[],\"sdesc\":\"무선충전에 무드등까지, 탁상시계가 다 할게요. 꿀잠에 든 라이언&콘에게 오늘의 온도와 알람도 맡겨보세요.\"}," +
                "{\"homeSeq\":237,\"badgeSeq\":0,\"type\":\"B\",\"mainImage\":\"https://t1.kakaocdn.net/friends/prod/main_tab/home/home_20200611112819_kr.jpg\",\"badgeName\":\"Good\",\"badgeColor\":\"#ff0011\",\"contentsType\":\"I\",\"title\":\"비도 오고 그래서\",\"linkUrl\":\"https://store.kakaofriends.com/kr/products/category/subject?categorySeq=13&subCategorySeq=33&sort=createDatetime,desc\",\"credate\":\"2020-06-11 11:28:19.376\",\"moddate\":\"2020-08-03 09:03:06.889\",\"creuser\":\"leah.koh\",\"moduser\":\"sam.hj\",\"dbsts\":\"A\",\"language\":\"kr\",\"displays\":[{\"dispGoodsId\":6936,\"imageUrl\":\"20200518155856283_8809721500699_AW_00.jpg\",\"discountSdate\":\"\",\"discountEdate\":\"\",\"discountStime\":\"00:00:00\",\"discountEtime\":\"23:59:59\",\"salePrice\":15000.0,\"discountPrice\":0.0,\"salePriceUsd\":13.97,\"discountPriceUsd\":0.0,\"defaultProductCode\":\"8809721500699\",\"foreignDeliCount\":\"0\",\"stockCount\":\"330\",\"basketCount\":\"0\",\"name\":\"데일리 장우산 라이언\",\"content\":\"\",\"soldOut\":false,\"discountPeriod\":false},{\"dispGoodsId\":6863,\"imageUrl\":\"20200427170208007_8809721500682_AW_00.jpg\",\"discountSdate\":\"\",\"discountEdate\":\"\",\"discountStime\":\"00:00:00\",\"discountEtime\":\"23:59:59\",\"salePrice\":9000.0,\"discountPrice\":0.0,\"salePriceUsd\":8.38,\"discountPriceUsd\":0.0,\"defaultProductCode\":\"8809721500682\",\"foreignDeliCount\":\"0\",\"stockCount\":\"80\",\"basketCount\":\"0\",\"name\":\"레몬테라스 투명우산 어피치\",\"content\":\"\",\"soldOut\":false,\"discountPeriod\":false},{\"dispGoodsId\":7010,\"imageUrl\":\"20200602155907459_8809721501108_AW_00.jpg\",\"discountSdate\":\"\",\"discountEdate\":\"\",\"discountStime\":\"00:00:00\",\"discountEtime\":\"23:59:59\",\"salePrice\":9000.0,\"discountPrice\":0.0,\"salePriceUsd\":8.38,\"discountPriceUsd\":0.0,\"defaultProductCode\":\"8809721501108\",\"foreignDeliCount\":\"0\",\"stockCount\":\"169\",\"basketCount\":\"0\",\"name\":\"마린 투명우산 어피치\",\"content\":\"\",\"soldOut\":false,\"discountPeriod\":false}],\"sdesc\":\"비도 오고 그래서 프렌즈 우산 살 이유가 생겼지 모야?! 미리미리 우산 준비하기 \"}," +
                "{\"homeSeq\":276,\"badgeSeq\":0,\"type\":\"D\",\"mainImage\":\"https://t1.kakaocdn.net/friends/prod/main_tab/home/home_20200721101004_kr.jpg\",\"badgeName\":\"Good\",\"badgeColor\":\"#ff0011\",\"contentsType\":\"I\",\"title\":\"케이스 고민 이제 그만 <br> 뭘 사도 성공이니까요\",\"linkUrl\":\"https://store.kakaofriends.com/kr/promotions/promotion/441\",\"credate\":\"2020-07-15 15:22:44.492\",\"moddate\":\"2020-08-03 10:49:36.777\",\"creuser\":\"leah.koh\",\"moduser\":\"sam.hj\",\"dbsts\":\"A\",\"language\":\"kr\",\"displays\":[{\"dispGoodsId\":6806,\"imageUrl\":\"20200420152322809_8809681708586_AW_00.jpg\",\"discountSdate\":\"\",\"discountEdate\":\"\",\"discountStime\":\"00:00:00\",\"discountEtime\":\"23:59:59\",\"salePrice\":17000.0,\"discountPrice\":0.0,\"salePriceUsd\":15.84,\"discountPriceUsd\":0.0,\"defaultProductCode\":\"8809681708586\",\"foreignDeliCount\":\"0\",\"stockCount\":\"148\",\"basketCount\":\"0\",\"name\":\"에어팟프로 케이스 리틀라이언\",\"content\":\"\",\"soldOut\":false,\"discountPeriod\":false},{\"dispGoodsId\":6938,\"imageUrl\":\"20200518163016448_8809721501924_AW_00.jpg\",\"discountSdate\":\"\",\"discountEdate\":\"\",\"discountStime\":\"00:00:00\",\"discountEtime\":\"23:59:59\",\"salePrice\":11000.0,\"discountPrice\":0.0,\"salePriceUsd\":10.25,\"discountPriceUsd\":0.0,\"defaultProductCode\":\"8809721501924\",\"foreignDeliCount\":\"0\",\"stockCount\":\"73\",\"basketCount\":\"0\",\"name\":\"버즈케이스 클리어 리틀라이언\",\"content\":\"\",\"soldOut\":false,\"discountPeriod\":false},{\"dispGoodsId\":6677,\"imageUrl\":\"20200305203627954_8809681706209_AW_00.jpg\",\"discountSdate\":\"\",\"discountEdate\":\"\",\"discountStime\":\"00:00:00\",\"discountEtime\":\"23:59:59\",\"salePrice\":15000.0,\"discountPrice\":0.0,\"salePriceUsd\":13.97,\"discountPriceUsd\":0.0,\"defaultProductCode\":\"8809681706209\",\"foreignDeliCount\":\"0\",\"stockCount\":\"144\",\"basketCount\":\"0\",\"name\":\"베이비드리밍 에어팟 케이스 글리터 리틀어피치\",\"content\":\"\",\"soldOut\":false,\"discountPeriod\":false},{\"dispGoodsId\":6886,\"imageUrl\":\"20200507113538454_8809721500354_AW_00.jpg\",\"discountSdate\":\"\",\"discountEdate\":\"\",\"discountStime\":\"00:00:00\",\"discountEtime\":\"23:59:59\",\"salePrice\":13000.0,\"discountPrice\":0.0,\"salePriceUsd\":12.11,\"discountPriceUsd\":0.0,\"defaultProductCode\":\"8809721500354\",\"foreignDeliCount\":\"0\",\"stockCount\":\"219\",\"basketCount\":\"0\",\"name\":\"얌얌 에어팟프로 케이스 어피치\",\"content\":\"\",\"soldOut\":false,\"discountPeriod\":false},{\"dispGoodsId\":6812,\"imageUrl\":\"20200424104736890_8809681709583_AW_00.jpg\",\"discountSdate\":\"\",\"discountEdate\":\"\",\"discountStime\":\"00:00:00\",\"discountEtime\":\"23:59:59\",\"salePrice\":17000.0,\"discountPrice\":0.0,\"salePriceUsd\":15.84,\"discountPriceUsd\":0.0,\"defaultProductCode\":\"8809681709583\",\"foreignDeliCount\":\"0\",\"stockCount\":\"152\",\"basketCount\":\"0\",\"name\":\"죠르디사과 에어팟프로케이스\",\"content\":\"\",\"soldOut\":false,\"discountPeriod\":false},{\"dispGoodsId\":6811,\"imageUrl\":\"20200423183322690_8809681709590_AW_00.jpg\",\"discountSdate\":\"\",\"discountEdate\":\"\",\"discountStime\":\"00:00:00\",\"discountEtime\":\"23:59:59\",\"salePrice\":17000.0,\"discountPrice\":0.0,\"salePriceUsd\":15.84,\"discountPriceUsd\":0.0,\"defaultProductCode\":\"8809681709590\",\"foreignDeliCount\":\"0\",\"stockCount\":\"332\",\"basketCount\":\"0\",\"name\":\"죠르디사과버즈케이스\",\"content\":\"\",\"soldOut\":false,\"discountPeriod\":false}],\"sdesc\":\"프로부터 버즈까지 기종 걱정 없이 취향대로 골라담아 콩나물 꾸미기\"}," +
                "{\"homeSeq\":279,\"badgeSeq\":0,\"type\":\"A\",\"mainImage\":\"https://t1.kakaocdn.net/friends/prod/main_tab/home/home_20200724114538_kr.jpg\",\"badgeName\":\"NEW\",\"badgeColor\":\"#3ed2e5\",\"contentsType\":\"I\",\"title\":\"마음까지 가벼운 <br> 청소시간 \",\"linkUrl\":\"https://store.kakaofriends.com/kr/promotions/promotion/434\",\"credate\":\"2020-07-24 11:45:39.572\",\"moddate\":\"2020-08-03 10:49:36.779\",\"creuser\":\"leah.koh\",\"moduser\":\"sam.hj\",\"dbsts\":\"A\",\"language\":\"kr\",\"displays\":[],\"sdesc\":\"쌓여가는 내 방 먼지에 무거워지는 마음, 프렌즈와 가볍게 털어내요. \"}," +
                "{\"homeSeq\":269,\"badgeSeq\":0,\"type\":\"D\",\"mainImage\":\"https://t1.kakaocdn.net/friends/prod/main_tab/home/home_20200709154542_kr.jpg\",\"badgeName\":\"+Friends\",\"badgeColor\":\"#2a46ff\",\"contentsType\":\"I\",\"title\":\"뽀짝템 싹쓰리하죨!\",\"linkUrl\":\"https://store.kakaofriends.com/kr/promotions/promotion/436\",\"credate\":\"2020-07-09 15:45:42.725\",\"moddate\":\"2020-08-03 10:49:36.78\",\"creuser\":\"leah.koh\",\"moduser\":\"sam.hj\",\"dbsts\":\"A\",\"language\":\"kr\",\"displays\":[{\"dispGoodsId\":6755,\"imageUrl\":\"20200402180112711_8809681708104_AW_00.jpg\",\"discountSdate\":\"\",\"discountEdate\":\"\",\"discountStime\":\"00:00:00\",\"discountEtime\":\"23:59:59\",\"salePrice\":22000.0,\"discountPrice\":0.0,\"salePriceUsd\":20.49,\"discountPriceUsd\":0.0,\"defaultProductCode\":\"8809681708104\",\"foreignDeliCount\":\"0\",\"stockCount\":\"262\",\"basketCount\":\"0\",\"name\":\"볼빵빵말랑쿠션 죠르디\",\"content\":\"\",\"soldOut\":false,\"discountPeriod\":false},{\"dispGoodsId\":7187,\"imageUrl\":\"20200708183543306_8809721502129_AW_00.jpg\",\"discountSdate\":\"\",\"discountEdate\":\"\",\"discountStime\":\"00:00:00\",\"discountEtime\":\"23:59:59\",\"salePrice\":5000.0,\"discountPrice\":0.0,\"salePriceUsd\":4.66,\"discountPriceUsd\":0.0,\"defaultProductCode\":\"8809721502129\",\"foreignDeliCount\":\"0\",\"stockCount\":\"524\",\"basketCount\":\"0\",\"name\":\"마우스패드_죠르디\",\"content\":\"\",\"soldOut\":false,\"discountPeriod\":false},{\"dispGoodsId\":6813,\"imageUrl\":\"20200423183342573_8809681709576_AW_00.jpg\",\"discountSdate\":\"\",\"discountEdate\":\"\",\"discountStime\":\"00:00:00\",\"discountEtime\":\"23:59:59\",\"salePrice\":17000.0,\"discountPrice\":0.0,\"salePriceUsd\":15.84,\"discountPriceUsd\":0.0,\"defaultProductCode\":\"8809681709576\",\"foreignDeliCount\":\"0\",\"stockCount\":\"323\",\"basketCount\":\"0\",\"name\":\"죠르디사과에어팟케이스\",\"content\":\"\",\"soldOut\":false,\"discountPeriod\":false},{\"dispGoodsId\":7192,\"imageUrl\":\"20200630152520819_8809721501986_AW_00.jpg\",\"discountSdate\":\"\",\"discountEdate\":\"\",\"discountStime\":\"00:00:00\",\"discountEtime\":\"23:59:59\",\"salePrice\":12000.0,\"discountPrice\":0.0,\"salePriceUsd\":11.18,\"discountPriceUsd\":0.0,\"defaultProductCode\":\"8809721501986\",\"foreignDeliCount\":\"0\",\"stockCount\":\"383\",\"basketCount\":\"0\",\"name\":\"피규어키링_사과죠르디\",\"content\":\"\",\"soldOut\":false,\"discountPeriod\":false},{\"dispGoodsId\":7117,\"imageUrl\":\"20200630114628703_8809721502907_AW_00.jpg\",\"discountSdate\":\"\",\"discountEdate\":\"\",\"discountStime\":\"00:00:00\",\"discountEtime\":\"23:59:59\",\"salePrice\":18000.0,\"discountPrice\":0.0,\"salePriceUsd\":16.77,\"discountPriceUsd\":0.0,\"defaultProductCode\":\"8809721502907\",\"foreignDeliCount\":\"0\",\"stockCount\":\"155\",\"basketCount\":\"0\",\"name\":\"페이스리드빨대컵_죠르디\",\"content\":\"\",\"soldOut\":false,\"discountPeriod\":false},{\"dispGoodsId\":7186,\"imageUrl\":\"20200630152742467_8809721502105_AW_00.jpg\",\"discountSdate\":\"\",\"discountEdate\":\"\",\"discountStime\":\"00:00:00\",\"discountEtime\":\"23:59:59\",\"salePrice\":18000.0,\"discountPrice\":0.0,\"salePriceUsd\":16.77,\"discountPriceUsd\":0.0,\"defaultProductCode\":\"\",\"foreignDeliCount\":\"0\",\"stockCount\":\"412\",\"basketCount\":\"0\",\"name\":\"그린포인트폰케이스_죠르디\",\"content\":\"\",\"soldOut\":false,\"discountPeriod\":false}],\"sdesc\":\"작고 소중한 죠르디 뽀짝템, 키링부터 파우치까지 놓치지 말고 싹쓰리하죨! \"}," +
                "{\"homeSeq\":234,\"badgeSeq\":0,\"type\":\"A\",\"mainImage\":\"https://t1.kakaocdn.net/friends/prod/main_tab/home/home_20200605152515_kr.jpg\",\"badgeName\":\"Good\",\"badgeColor\":\"#ff0011\",\"contentsType\":\"I\",\"title\":\"티브이 위 라이언 <br> 레트로 감성 충전\",\"linkUrl\":\"https://store.kakaofriends.com/kr/products/7097\",\"credate\":\"2020-06-05 15:25:15.89\",\"moddate\":\"2020-08-03 10:49:36.781\",\"creuser\":\"leah.koh\",\"moduser\":\"sam.hj\",\"dbsts\":\"A\",\"language\":\"kr\",\"displays\":[],\"sdesc\":\"핸드폰도, 레트로 감성도, 라이언 덕력도 함께 충전하는 만능템\"}," +
                "{\"homeSeq\":238,\"badgeSeq\":0,\"type\":\"C\",\"mainImage\":\"https://t1.kakaocdn.net/friends/prod/main_tab/home/home_20200622141941_kr.jpg\",\"contentsType\":\"I\",\"title\":\"[온라인 전용]탁상 선풍기\",\"linkUrl\":\"https://store.kakaofriends.com/kr/products/7121\",\"credate\":\"2020-06-12 16:38:47.4\",\"moddate\":\"2020-08-03 10:49:36.782\",\"creuser\":\"leah.koh\",\"moduser\":\"sam.hj\",\"dbsts\":\"A\",\"language\":\"kr\",\"displays\":[]}," +
                "{\"homeSeq\":141,\"badgeSeq\":0,\"type\":\"C\",\"mainImage\":\"https://t1.kakaocdn.net/friends/new_store/prod/main_tab/home/home_20200730111737_kr.jpg\",\"contentsType\":\"I\",\"title\":\"배경화면\",\"linkUrl\":\"https://store.kakaofriends.com/kr/brand/wallpaper202008\",\"credate\":\"2019-12-20 10:36:57.699\",\"moddate\":\"2020-08-03 10:49:36.783\",\"creuser\":\"leah.koh\",\"moduser\":\"sam.hj\",\"dbsts\":\"A\",\"language\":\"kr\",\"displays\":[]}," +
                "{\"homeSeq\":272,\"badgeSeq\":0,\"type\":\"D\",\"mainImage\":\"https://t1.kakaocdn.net/friends/prod/main_tab/home/home_20200713110040_kr.jpg\",\"badgeName\":\"+Friends\",\"badgeColor\":\"#2a46ff\",\"contentsType\":\"I\",\"title\":\"매일매일 사용해도 <br> 줄지않는 귀여움\",\"linkUrl\":\"https://store.kakaofriends.com/kr/promotions/promotion/432\",\"credate\":\"2020-07-10 11:10:48.01\",\"moddate\":\"2020-08-03 09:03:06.898\",\"creuser\":\"leah.koh\",\"moduser\":\"sam.hj\",\"dbsts\":\"A\",\"language\":\"kr\",\"displays\":[{\"dispGoodsId\":7185,\"imageUrl\":\"20200703155210761_8809721500958_AW_00.jpg\",\"discountSdate\":\"\",\"discountEdate\":\"\",\"discountStime\":\"00:00:00\",\"discountEtime\":\"23:59:59\",\"salePrice\":19000.0,\"discountPrice\":0.0,\"salePriceUsd\":17.7,\"discountPriceUsd\":0.0,\"defaultProductCode\":\"8809721500958\",\"foreignDeliCount\":\"0\",\"stockCount\":\"175\",\"basketCount\":\"0\",\"name\":\"포레스트 피규어 펜홀더_라이언\",\"content\":\"\",\"soldOut\":false,\"discountPeriod\":false},{\"dispGoodsId\":6991,\"imageUrl\":\"20200622121627494_8809681709569_AW_00.jpg\",\"discountSdate\":\"\",\"discountEdate\":\"\",\"discountStime\":\"00:00:00\",\"discountEtime\":\"23:59:59\",\"salePrice\":29000.0,\"discountPrice\":0.0,\"salePriceUsd\":27.02,\"discountPriceUsd\":0.0,\"defaultProductCode\":\"8809681709569\",\"foreignDeliCount\":\"0\",\"stockCount\":\"376\",\"basketCount\":\"0\",\"name\":\"리틀프렌즈피규어세트\",\"content\":\"\",\"soldOut\":false,\"discountPeriod\":false},{\"dispGoodsId\":7155,\"imageUrl\":\"20200625154848168_8809721502600_AW_00.jpg\",\"discountSdate\":\"\",\"discountEdate\":\"\",\"discountStime\":\"00:00:00\",\"discountEtime\":\"23:59:59\",\"salePrice\":15000.0,\"discountPrice\":0.0,\"salePriceUsd\":13.97,\"discountPriceUsd\":0.0,\"defaultProductCode\":\"8809721502600\",\"foreignDeliCount\":\"0\",\"stockCount\":\"113\",\"basketCount\":\"0\",\"name\":\"카드&동전지갑_리틀라이언\",\"content\":\"\",\"soldOut\":false,\"discountPeriod\":false},{\"dispGoodsId\":7147,\"imageUrl\":\"20200708151902407_8809721502501_AW_00.jpg\",\"discountSdate\":\"\",\"discountEdate\":\"\",\"discountStime\":\"00:00:00\",\"discountEtime\":\"23:59:59\",\"salePrice\":10000.0,\"discountPrice\":0.0,\"salePriceUsd\":9.32,\"discountPriceUsd\":0.0,\"defaultProductCode\":\"8809721502501\",\"foreignDeliCount\":\"0\",\"stockCount\":\"122\",\"basketCount\":\"0\",\"name\":\"베이직 카드지갑_리틀어피치\",\"content\":\"\",\"soldOut\":false,\"discountPeriod\":false},{\"dispGoodsId\":7165,\"imageUrl\":\"20200708185806288_8809721502464_AW_00.jpeg\",\"discountSdate\":\"\",\"discountEdate\":\"\",\"discountStime\":\"00:00:00\",\"discountEtime\":\"23:59:59\",\"salePrice\":11000.0,\"discountPrice\":0.0,\"salePriceUsd\":10.25,\"discountPriceUsd\":0.0,\"defaultProductCode\":\"8809721502464\",\"foreignDeliCount\":\"0\",\"stockCount\":\"308\",\"basketCount\":\"0\",\"name\":\"큐브 에어팟키링_리틀라이언\",\"content\":\"\",\"soldOut\":false,\"discountPeriod\":false},{\"dispGoodsId\":7168,\"imageUrl\":\"20200708185421774_8809721502433_AW_00.jpg\",\"discountSdate\":\"\",\"discountEdate\":\"\",\"discountStime\":\"00:00:00\",\"discountEtime\":\"23:59:59\",\"salePrice\":9000.0,\"discountPrice\":0.0,\"salePriceUsd\":8.38,\"discountPriceUsd\":0.0,\"defaultProductCode\":\"8809721502433\",\"foreignDeliCount\":\"0\",\"stockCount\":\"334\",\"basketCount\":\"0\",\"name\":\"피규어 에어팟키링_어피치\",\"content\":\"\",\"soldOut\":false,\"discountPeriod\":false}],\"sdesc\":\"역대급 귀염미로 무장한 키링부터 지갑, 파우치까지. 매일 써도 귀여움은 줄지 않는다구요!\"}," +
                "{\"homeSeq\":267,\"badgeSeq\":0,\"type\":\"D\",\"mainImage\":\"https://t1.kakaocdn.net/friends/prod/main_tab/home/home_20200706140321_kr.jpg\",\"badgeName\":\"+Friends\",\"badgeColor\":\"#2a46ff\",\"contentsType\":\"I\",\"title\":\"착한 얼굴에 짐승용량, <br> 배터리 걱정 끝! \",\"linkUrl\":\"https://store.kakaofriends.com/kr/promotions/promotion/431\",\"credate\":\"2020-07-06 14:03:21.838\",\"moddate\":\"2020-07-31 14:29:50.188\",\"creuser\":\"leah.koh\",\"moduser\":\"sam.hj\",\"dbsts\":\"A\",\"language\":\"kr\",\"displays\":[{\"dispGoodsId\":7182,\"imageUrl\":\"20200630153205496_8809721501856_AW_00.jpg\",\"discountSdate\":\"\",\"discountEdate\":\"\",\"discountStime\":\"00:00:00\",\"discountEtime\":\"23:59:59\",\"salePrice\":59000.0,\"discountPrice\":0.0,\"salePriceUsd\":54.96,\"discountPriceUsd\":0.0,\"defaultProductCode\":\"8809721501856\",\"foreignDeliCount\":\"0\",\"stockCount\":\"671\",\"basketCount\":\"0\",\"name\":\"10,000mAh 고속보조배터리_라이언\",\"content\":\"\",\"soldOut\":false,\"discountPeriod\":false},{\"dispGoodsId\":7181,\"imageUrl\":\"20200630153318954_8809721501863_AW_00.jpg\",\"discountSdate\":\"\",\"discountEdate\":\"\",\"discountStime\":\"00:00:00\",\"discountEtime\":\"23:59:59\",\"salePrice\":59000.0,\"discountPrice\":0.0,\"salePriceUsd\":54.96,\"discountPriceUsd\":0.0,\"defaultProductCode\":\"8809721501863\",\"foreignDeliCount\":\"0\",\"stockCount\":\"754\",\"basketCount\":\"0\",\"name\":\"10,000mAh 고속보조배터리_어피치\",\"content\":\"\",\"soldOut\":false,\"discountPeriod\":false},{\"dispGoodsId\":7180,\"imageUrl\":\"20200701180257216_8809721501870_AW_00.jpg\",\"discountSdate\":\"\",\"discountEdate\":\"\",\"discountStime\":\"00:00:00\",\"discountEtime\":\"23:59:59\",\"salePrice\":18000.0,\"discountPrice\":0.0,\"salePriceUsd\":16.77,\"discountPriceUsd\":0.0,\"defaultProductCode\":\"8809721501870\",\"foreignDeliCount\":\"0\",\"stockCount\":\"474\",\"basketCount\":\"0\",\"name\":\"릴타입 3in1 케이블_라이언\",\"content\":\"\",\"soldOut\":false,\"discountPeriod\":false},{\"dispGoodsId\":7179,\"imageUrl\":\"20200701180454547_8809721501887_AW_00.jpg\",\"discountSdate\":\"\",\"discountEdate\":\"\",\"discountStime\":\"00:00:00\",\"discountEtime\":\"23:59:59\",\"salePrice\":18000.0,\"discountPrice\":0.0,\"salePriceUsd\":16.77,\"discountPriceUsd\":0.0,\"defaultProductCode\":\"8809721501887\",\"foreignDeliCount\":\"0\",\"stockCount\":\"447\",\"basketCount\":\"0\",\"name\":\"릴타입 3in1 케이블_어피치\",\"content\":\"\",\"soldOut\":false,\"discountPeriod\":false},{\"dispGoodsId\":6069,\"imageUrl\":\"8809681700313_AW_00.jpg\",\"discountSdate\":\"\",\"discountEdate\":\"\",\"discountStime\":\"00:00:00\",\"discountEtime\":\"23:59:59\",\"salePrice\":49000.0,\"discountPrice\":0.0,\"salePriceUsd\":45.65,\"discountPriceUsd\":0.0,\"defaultProductCode\":\"8809681700313\",\"foreignDeliCount\":\"0\",\"stockCount\":\"79\",\"basketCount\":\"0\",\"name\":\"라이언 듀얼 고속 충전 보조배터리 10,000mAh\",\"content\":\"\",\"soldOut\":false,\"discountPeriod\":false},{\"dispGoodsId\":6068,\"imageUrl\":\"8809681700320_AW_00.jpg\",\"discountStime\":\"00:00:00\",\"discountEtime\":\"23:59:59\",\"salePrice\":49000.0,\"discountPrice\":0.0,\"salePriceUsd\":45.65,\"discountPriceUsd\":0.0,\"defaultProductCode\":\"8809681700320\",\"foreignDeliCount\":\"0\",\"stockCount\":\"34\",\"basketCount\":\"0\",\"name\":\"어피치 듀얼 고속 충전 보조배터리 10,000mAh\",\"content\":\"\",\"soldOut\":false,\"discountPeriod\":false}],\"sdesc\":\"든든한 용량에 귀여움까지 갖춘 보배 찾고있다면? 당신의 종착지는 바로 여기! \"}]}";
    }

    public String getMembersBasketJson() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "{\"userId\":\"8b28009c-0130-4e9a-a32a-5e5c81dfd7b9\",\"basketCount\":0}";
    }

}
