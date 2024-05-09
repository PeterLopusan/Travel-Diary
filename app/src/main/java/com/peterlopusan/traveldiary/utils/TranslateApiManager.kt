package com.peterlopusan.traveldiary.utils

import com.peterlopusan.traveldiary.MainActivity
import com.peterlopusan.traveldiary.R
import com.peterlopusan.traveldiary.africa
import com.peterlopusan.traveldiary.antarctica
import com.peterlopusan.traveldiary.asia
import com.peterlopusan.traveldiary.data.models.country.Country
import com.peterlopusan.traveldiary.europe
import com.peterlopusan.traveldiary.northAmerica
import com.peterlopusan.traveldiary.oceania
import com.peterlopusan.traveldiary.sharedPreferences.SharedPreferencesManager
import com.peterlopusan.traveldiary.southAmerica
import java.util.Locale

class TranslateApiManager {
    fun translateCountryList(countryList: MutableList<Country>): MutableList<Country> {
        val language = SharedPreferencesManager().getLanguage()

        if (language == "SK" || Locale.getDefault().language == "sk") {
            countryList.forEach {
                val native = it.name?.nativeName
                it.name = it.translations?.slk
                it.name?.nativeName = native
            }
        } else {
            countryList.forEach {
                val native = it.name?.nativeName
                it.name = it.translations?.eng
                it.name?.nativeName = native
            }
        }

        return countryList
    }

    fun translateContinent(continent: String): String {
        val language = SharedPreferencesManager().getLanguage()
        var translatedContinent = continent

        if (language == "SK" || Locale.getDefault().language == "sk") {
            translatedContinent = when (continent) {
                europe -> { MainActivity.instance.getString(R.string.europe) }
                asia -> MainActivity.instance.getString(R.string.asia)
                africa -> MainActivity.instance.getString(R.string.africa)
                oceania -> MainActivity.instance.getString(R.string.oceania)
                antarctica -> MainActivity.instance.getString(R.string.antarctica)
                northAmerica -> MainActivity.instance.getString(R.string.north_america)
                southAmerica -> MainActivity.instance.getString(R.string.south_america)
                else -> continent
            }
        }

        return translatedContinent
    }

    fun translateSubregion(subregion: String?): String? {
        val language = SharedPreferencesManager().getLanguage()
        var translatedSubregion = subregion

        if (language == "SK" || Locale.getDefault().language == "sk") {
            translatedSubregion = when (subregion) {
                "Eastern Europe" -> "Východná Európa"
                "Southeast Europe" -> "Juhovýchodná Európa"
                "Northern Europe" -> "Severná Európa"
                "Southern Europe" -> "Južná Európa"
                "Central Europe" -> "Stredná Európa"
                "Western Europe" -> "Západná Európa"

                "Western Asia" -> "Západná Ázia"
                "South-Eastern Asia" -> "Juhovýchodná Ázia"
                "Southern Asia" -> "Južná Ázia"
                "Central Asia" -> "Stredná Ázia"
                "Eastern Asia" -> "Východná Ázia"

                "Eastern Africa" -> "Východná Afrika"
                "Western Africa" -> "Západná Afrika"
                "Middle Africa" -> "Stredná Afrika"
                "Northern Africa" -> "Severná Afrika"
                "Southern Africa" -> "Južná Afrika"

                "North America" -> "Severná Amerika"
                "Caribbean" -> "Karibik"

                "Central America" -> "Stredná Amerika"

                "South America" -> "Južná Amerika"

                "Micronesia" -> "Mikronézia"
                "Polynesia" -> "Polynézia"
                "Melanesia" -> "Melanézia"
                "Australia and New Zealand" -> "Austrália a Nový Zéland"

                else -> subregion
            }
        }

        return translatedSubregion
    }

    fun translateLanguage(language: String?): String? {
        val selectedLanguage = SharedPreferencesManager().getLanguage()
        var translatedLanguage = language

        if (selectedLanguage == "SK" || Locale.getDefault().language == "sk") {
            translatedLanguage = when (language) {
                "Albanian" -> "albančina"
                "Armenian" -> "armenčina"
                "Amharic" -> "amharčina"
                "Arabic" -> "arabčina"
                "Aymara" -> "aymaračina"
                "Aramaic" -> "aramejčina"
                "Azerbaijani" -> "azerbajdžančina"
                "Afrikaans" -> "afrikánčina"
                "Berber" -> "berberčina"
                "Burmese" -> "barmčina"
                "Basque" -> "baskičtina"
                "Bosnian" -> "bosniančina"
                "Bengali" -> "bengálčina"
                "Bulgarian" -> "bulharčina"
                "Belarusian" -> "bieloruština"
                "Croatian" -> "chorvátčina"
                "Catalan" -> "katalánčina"
                "Comorian" -> "komorčina"
                "Czech" -> "čeština"
                "Chinese" -> "čínština"
                "Dutch" -> "holandčina"
                "Danish" -> "dánčina"
                "English" -> "angličtina"
                "Estonian" -> "estónčina"
                "French" -> "francúzština"
                "Finnish" -> "fínčina"
                "Filipino" -> "filipínčina"
                "Faroese" -> "faerčina"
                "Fiji Hindi" -> "Fidžíjska hindčina"
                "Greek" -> "gréčtina"
                "Galician" -> "galícijčina"
                "Greenlandic" -> "grónčina"
                "German" -> "nemčina"
                "Guaraní" -> "guaraníjčina"
                "Georgian" -> "gruzínčina"
                "Hassaniya" -> "hassaniyaska arabčina"
                "Hungarian" -> "maďarčina"
                "Hindi" -> "hindčina"
                "Hebrew" -> "hebrejčina"
                "Irish" -> "Írčina"
                "Italian" -> "taliančina"
                "Icelandic" -> "islandčina"
                "Indonesian" -> "indonézčina"
                "Japanese" -> "japončina"
                "Kazakh" -> "kazaština"
                "Korean" -> "korejčina"
                "Khmer" -> "khméri"
                "Kyrgyz" -> "kirgizština"
                "Latvian" -> "lotyština"
                "Luxembourgish" -> "luxemburčina"
                "Latin" -> "latinčina"
                "Lithuanian" -> "litovčina"
                "Malay" -> "malajčina"
                "Māori" -> "maorijčina"
                "Macedonian" -> "macedónčina"
                "Marshallese" -> "maršálčina"
                "Malagasy" -> "malgaština"
                "Maldivian" -> "maldivčina"
                "Montenegrin" -> "čiernohorčina"
                "Mongolian" -> "mongolčina"
                "Nauru" -> "nauruština"
                "Norwegian" -> "nórčina"
                "Maltese" -> "maltčina"
                "Norwegian Nynorsk" -> "nynorsk"
                "Norwegian Bokmål" -> "bokmål"
                "Niuean" -> "niuejčina"
                "Nepali" -> "nepálčina"
                "New Zealand Sign Language" -> "novozélandský posunkový jazyk"
                "Palauan" -> "palaučina"
                "Portuguese" -> "portugalčina"
                "Persian (Farsi)" -> "perzština"
                "Polish" -> "polština"
                "Quechua" -> "quechuačima"
                "Romanian" -> "rumunčina"
                "Romansh" -> "rétorománčina"
                "Russian" -> "ruština"
                "Spanish" -> "španielčina"
                "Sotho" -> "sesothčina"
                "Swiss German" -> "švajčiarska nemčina"
                "Slovak" -> "slovenčina"
                "Slovene" -> "slovinčina"
                "Serbian" -> "srbčina"
                "Somali" -> "somálčina"
                "Sinhala" -> "sinhalčina"
                "Swedish" -> "Švédčina"
                "Swahili" -> "svahilčina"
                "Sami" -> "lapončina"
                "Turkish" -> "turečtina"
                "Tigrinya" -> "tigriňa"
                "Turkmen" -> "turkménčina"
                "Tajik" -> "tadžičtina"
                "Tamil" -> "tamilčina"
                "Thai" -> "thajčina"
                "Tuvaluan" -> "tuvalčina"
                "Tongan" -> "tongčina"
                "Uzbek" -> "uzbečtina"
                "Ukrainian" -> "ukrajinčina"
                "Vietnamese" -> "vietnamčina"
                "Zimbabwean Sign Language" -> "Zimbabwiansky posunkový jazyk"
                else -> language
            }
        }
        return translatedLanguage
    }

    fun translateCapital(capital: String?): String? {
        val language = SharedPreferencesManager().getLanguage()
        var translatedCapital = capital

        if (language == "SK" || Locale.getDefault().language == "sk") {
            translatedCapital = when (capital) {
                "Chișinău" -> "Kišiňov"
                "Brasília" -> "Brazília"
                "Mogadishu" -> "Mogadišo"
                "London" -> "Londýn"
                "Nicosia" -> "Nikózia"
                "Pretoria" -> "Pretória"
                "Cape Town" -> "Kapské mesto"
                "Kuwait City" -> "Kuwajt"
                "Algiers" -> "Alžír"
                "Zagreb" -> "Záhreb"
                "Pristina" -> "Priština"
                "Vienna" -> "Viedeň"
                "Prague" -> "Praha"
                "Paris" -> "Paríž"
                "Brussels" -> "Brusel"
                "Kyiv" -> "Kyjev"
                "Tehran" -> "Teherán"
                "Bucharest" -> "Bukurešť"
                "Bishkek" -> "Biškek"
                "Yerevan" -> "Jerevan"
                "Beirut" -> "Bejrút"
                "Stockholm" -> "Štokholm"
                "Beijing" -> "Peking"
                "Moscow" -> "Moskva"
                "Pyongyang" -> "Pchjongjang"
                "Lisbon" -> "Lisabon"
                "Warsaw" -> "Varšava"
                "Copenhagen" -> "Kodaň"
                "Ljubljana" -> "Ľubľana"
                "Athens" -> "Atény"
                "Jerusalem" -> "Jeruzalem"
                "Kabul" -> "Kábul"
                "Tripoli" -> "Tripoli"
                "Berlin" -> "Berlín"
                "Riyadh" -> "Rijád"
                "Seoul" -> "Soul"
                "Singapore" -> "Singapur"
                "Budapest" -> "Budapešť"
                "Reykjavik" -> "Reykjavík"
                "Cairo" -> "Káhira"
                "Belgrade" -> "Belehrad"
                "Rome" -> "Rím"
                "Baghdad" -> "Bagdad"
                "Tokyo" -> "Tokio"
                else -> translatedCapital
            }
        }

        //skončil som na 150
        return translatedCapital
    }

    fun translateCurrency(currency: String?): String? {
        val language = SharedPreferencesManager().getLanguage()
        var translatedCurrency = currency

        if (language == "SK" || Locale.getDefault().language == "sk") {
            translatedCurrency = when (currency) {
                "Moldovan leu" -> "Moldavský lei"
                "United States dollar" -> "Americký dolár"
                "Australian dollar" -> "Austrálsky dolár"
                "Mozambican metical" -> "Mozambický metical"
                "Brazilian real" -> "Brazílsky real"
                "Cape Verdean escudo" -> "Kapverdské escudo"
                "Central African CFA franc" -> "Stredoafrický frank"
                "Albanian lek" -> "Albánsky lek"
                "New Zealand dollar" -> "Novozélandský dolár"
                "Nigerian naira" -> "Naira"
                "dalasi" -> "Gambijský dalasi"
                "Somali shilling" -> "Somálský šilink"
                "Yemeni rial" -> "Jemenský rial"
                "Malaysian ringgit" -> "Malajzíjsky ringgit"
                "Eastern Caribbean dollar" -> "Východokaribský dolár"
                "British pound" -> "Britská libra"
                "Malagasy ariary" -> "Malgašský ariary"
                "Algerian dinar" -> "Alžírský dinár"
                "Paraguayan guaraní" -> "Guarani"
                "Sri Lankan rupee" -> "Srílanská rupia"
                "South African rand" -> "Rand"
                "Kuwaiti dinar" -> "Kuvajtský dinár"
                "Sierra Leonean leone" -> "Sierraleonský leone"
                "Rwandan franc" -> "Rwandský frank"
                "Syrian pound" -> "Sýrska libra"
                "Honduran lempira" -> "Honduraská lempira"
                "Jordanian dinar" -> "Jordánský dinár"
                "Nepalese rupee" -> "Nepálska rupia"
                "Liberian dollar" -> "Libérijský dolár"
                "Mauritanian ouguiya" -> "Mauritánská ouguiya"
                "Djiboutian franc" -> "Džibutský frank"
                "Fijian dollar" -> "Fidžijský dolár"
                "Norwegian krone" -> "Nórska koruna"
                "Falkland Islands pound" -> "Falklandská libra"
                "Kazakhstani tenge" -> "Kazašský tenge"
                "Turkmenistan manat" -> "Turkménsky manat"
                "Bulgarian lev" -> "Bulharský lev"
                "CFP franc" -> "CFP frank"
                "Barbadian dollar" -> "Barbadoský dolár"
                "São Tomé and Príncipe dobra" -> "Dobra"
                "Brunei dollar" -> "Brunejský dolár "
                "Bhutanese ngultrum" -> "Ngultrum"
                "Argentine peso" -> "Argentínske peso"
                "Azerbaijani manat" -> "Azerbajdžanský manat"
                "Mexican peso" -> "Mexické peso"
                "Moroccan dirham" -> "Marocký dirham"
                "Guatemalan quetzal" -> "Guatemalský quetzal"
                "Kenyan shilling" -> "Kenský šiling"
                "Czech koruna" -> "Česká koruna"
                "Gibraltar pound" -> "Gibraltárska libra"
                "Aruban florin" -> "Arubský florin"
                "United Arab Emirates dirham" -> "Emirátsky dirham"
                "South Sudanese pound" -> "Juhosudánska libra"
                "West African CFA franc" -> "Západoafrický frank"
                "Saint Helena pound" -> "Sväatohelenská libra"
                "Botswana pula" -> "Pula"
                "Uzbekistani soʻm" -> "Uzbecký som"
                "Tunisian dinar" -> "Tuniský dinár"
                "Hong Kong dollar" -> "Hongkongský dolár"
                "denar" -> "Macedónsky denár"
                "Surinamese dollar" -> "Surinamský dolár"
                "Solomon Islands dollar" -> "Dolár Šalamúnových ostrovov"
                "Ukrainian hryvnia" -> "Ukrajinská hrivna"
                "Bosnia and Herzegovina convertible mark" -> "bosnianskohercegovinská marka"
                "Iranian rial" -> "Iránsky rial"
                "Cuban convertible peso" -> "Kubánske peso"
                "Eritrean nakfa" -> "Eritrejská nakfa"
                "Turkish lira" -> "Turecká líra"
                "Philippine peso" -> "Filipínske peso"
                "Vanuatu vatu" -> "Vanuatuský vatu"
                "Bolivian boliviano" -> "Bolívijský boliviano"
                "Romanian leu" -> "Rumunský leu"
                "Cambodian riel" -> "Riel"
                "Zimbabwean dollar" -> "Americký dolár"
                "Kyrgyzstani som" -> "Kyrgizský som"
                "Guyanese dollar" -> "Guyanský dolár"
                "Armenian dram" -> "Arménsky dram"
                "Lebanese pound" -> "Libanonská libra"
                "krone" -> "Dánska koruna"
                "Papua New Guinean kina" -> "Kina"
                "Zambian kwacha" -> "Zambíjska kwacha"
                "Trinidad and Tobago dollar" -> "Trinidadsko-tobažský dolár"
                "Peruvian sol" -> "Sol"
                "Swedish krona" -> "Švédska koruna"
                "Sudanese pound" -> "Sudánska libra"
                "Omani rial" -> "Ománsky rial"
                "Indian rupee" -> "Indická rupia"
                "New Taiwan dollar" -> "Nový taiwanský dolár"
                "Mongolian tögrög" -> "Mongolský tugrik"
                "Tanzanian shilling" -> "Tanzánijský šiling"
                "Canadian dollar" -> "Kanadský dolár"
                "Costa Rican colón" -> "Kostarický colón"
                "Chinese yuan" -> "Jüan"
                "Colombian peso" -> "Kolumbíjske peso"
                "Burmese kyat" -> "Kyat"
                "Russian ruble" -> "Ruský rubeľ"
                "North Korean won" -> "Severokórejský won"
                "Cayman Islands dollar" -> "Kajmanský dolár"
                "Belarusian ruble" -> "Bieloruský rubeľ"
                "Swazi lilangeni" -> "Lilangeni"
                "Polish złoty" -> "Poľský zlotý"
                "Swiss franc" -> "Švajčiarsky frank"
                "Venezuelan bolívar soberano" -> "Venezuelský bolívar"
                "Panamanian balboa" -> "Panamská balboa"
                "Samoan tālā" -> "Tala"
                "Danish krone" -> "Dánska koruna"
                "Faroese króna" -> "Faerská koruna"
                "Thai baht" -> "Baht"
                "Bahamian dollar" -> "Bahamský dolár"
                "Tongan paʻanga" -> "Tongská paʻanga"
                "Burundian franc" -> "Burundský frank"
                "Bahraini dinar" -> "Bahrajnský dinar"
                "Haitian gourde" -> "Haitský gourde"
                "Afghan afghani" -> "Afghani"
                "Israeli new shekel" -> "Nový izraelský šekel"
                "Libyan dinar" -> "Líbyjský dinár"
                "Uruguayan peso" -> "Uruguajské peso"
                "Nicaraguan córdoba" -> "Córdoba"
                "Cook Islands dollar" -> "Dolár Cookových ostrovov"
                "Lao kip" -> "Laoský kip "
                "Pound sterling" -> "Anglická libra"
                "Kiribati dollar" -> "Kiribatský dolár"
                "Netherlands Antillean guilder" -> "Guilder"
                "Jamaican dollar" -> "Jamajský dolár"
                "Egyptian pound" -> "Egyptská libra"
                "Chilean peso" -> "Čilské peso"
                "Lesotho loti" -> "Loti"
                "Ghanaian cedi" -> "Ghanský cedi"
                "Seychellois rupee" -> "Seychelská rupia"
                "Angolan kwanza" -> "Angolská kwanza"
                "Bermudian dollar" -> "Bermúdsky dolár"
                "Pakistani rupee" -> "Pakistánska rupia"
                "Saudi riyal" -> "Saudskoarabský rial"
                "South Korean won" -> "Juhokorejský won"
                "Ethiopian birr" -> "Etiópsky birr"
                "Bangladeshi taka" -> "Taka"
                "Comorian franc" -> "Komorský frank"
                "Belize dollar" -> "Belizejský dolár"
                "Ugandan shilling" -> "Ugandský šiling"
                "Singapore dollar" -> "Singapurský dolár"
                "Hungarian forint" -> "Forint"
                "Icelandic króna" -> "Islandská koruna"
                "Tajikistani somoni" -> "Somoni"
                "Namibian dollar" -> "Namíbijský dolár"
                "Serbian dinar" -> "Srbský dinar"
                "Mauritian rupee" -> "Maurícijská rupia"
                "Macanese pataca" -> "Macauská pataca"
                "Maldivian rufiyaa" -> "Rufiyaa"
                "Indonesian rupiah" -> "Indonézska rupia"
                "Congolese franc"-> "Konžský frank"
                "Vietnamese đồng" -> "Vietnamský đồng"
                "Guinean franc" -> "Guinejský frank"
                "Malawian kwacha" -> "Malawijská kwacha"
                "Iraqi dinar" -> "Iracký dinár"
                "Japanese yen" -> "Jen"
                "Dominican peso" -> "Dominikánske peso"
                "Qatari riyal" -> "Katarský rijál"
                else -> currency
            }
        }

        return translatedCurrency
    }
}