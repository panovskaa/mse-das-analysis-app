from transformers import pipeline
import csv
import pandas as pd
import requests
import json
import sys
from bs4 import BeautifulSoup

company_ids = {
    '116': 'Avioimpeks AD Skopje',
    '68': 'Agrokumanovo AD Kumanovo',
    '96': 'Agromehanika AD Skopje',
    '9': 'Agroplod AD Resen',
    '122': 'Ading AD Skopje',
    '10': 'Alkaloid AD Skopje',
    '109': 'Angropromet Tikveshanka AD Kavadarci',
    '139': 'ArcelorMittal (HRM) AD Skopje',
    '11': 'Automakedonija AD Skopje',
    '12': 'Beton AD Skopje',
    '117': 'Beton AD Shtip',
    '97': 'BIM AD Sveti Nikole',
    '67': 'Blagoj Tufanov AD Radovish',
    '106': 'Vabtek MZT AD Skopje',
    '14': 'VV Tikvesh AD Kavadarci',
    '132': 'Velestabak AD Veles',
    '112': 'Veteks AD Veles',
    '15': 'Vitaminka AD Prilep',
    '64': 'GD Tikvesh AD Kavadarci',
    '125': 'Geras Cunev Konfekcija AD Strumica',
    '124': 'Geras Cunev Trgovija AD Strumica',
    '16': 'Granit AD Skopje',
    '84': 'Grozd AD Strumica',
    '92': 'GTC AD Skopje',
    '77': 'Debarski banji - Capa AD Debar',
    '17': 'Dimko Mitrev AD Veles',
    '30': 'DS Smith AD Skopje',
    '18': 'Evropa AD Skopje',
    '89': 'Edinstvo AD Strumica',
    '131': 'Edinstvo AD Tetovo',
    '19': 'EMO AD Ohrid',
    '123': 'ZHAS AD Skopje',
    '20': 'Zhito Bitola AD',
    '21': 'Zhito Vardar AD Veles',
    '140': 'Zhito Karaorman AD Kichevo',
    '22': 'Zhito Luks AD Skopje',
    '121': 'Zhito Polog AD Tetovo',
    '136': 'Zhito Skopje AD Skopje',
    '23': 'ZK Pelagonija AD Bitola',
    '118': 'Idevelop AD Skopje',
    '25': 'Interneshnel Hotels AD Skopje',
    '127': 'Interpromet AD Tetovo',
    '137': 'Jaka 80 AD Radovish',
    '141': 'Karbo Nova AD Kriva Palanka',
    '26': 'Karposh AD Skopje',
    '28': 'Kiro Kjuchuk AD Veles',
    '102': 'Klanica so ladilnik AD Strumica',
    '29': 'Komercijalna Banka AD Skopje',
    '13': 'Kristal 1923 AD Veles',
    '138': 'Liberti AD Skopje',
    '31': 'Lotarija na Makedonija AD Skopje',
    '32': 'Mavrovo ADG',
    '81': 'Makedonija AD Bitola',
    '93': 'Makedonija osiguruvanje AD Skopje',
    '33': 'Makedonijaturist AD Skopje',
    '82': 'Makedonski Telekom AD Skopje',
    '34': 'Makoteks AD Skopje',
    '35': 'Makoshped AD Skopje',
    '36': 'Makpetrol AD Skopje',
    '76': 'Makpromet AD Shtip',
    '37': 'Makstil AD Skopje',
    '94': 'Mermeren kombinat AD Prilep',
    '126': 'MZT Pumpi AD Skopje',
    '66': 'Moda AD Sveti Nikole',
    '99': 'Nemetali Ograzhden AD Strumica',
    '108': 'NLB Banka AD Skopje',
    '114': 'Nova stokovna kukja AD Strumica',
    '88': 'OILKO KDA Skopje',
    '134': 'OKTA AD Skopje',
    '115': 'Oranzherii Hamzali AD Strumica',
    '38': 'Oteks',
    '39': 'Ohis AD Skopje',
    '40': 'Ohridska Banka AD Skopje',
    '107': 'Patnichki soobrakjaj - Transkop AD Bitola',
    '142': 'Pekabesko AD Kadino, Ilinden',
    '70': 'Pelisterka AD Skopje',
    '72': 'Popova kula AD Demir Kapija',
    '69': 'Prilepska pivarnica AD Prilep',
    '101': 'Rade Konchar - Aparatna tehnika AD Skopje',
    '41': 'Replek AD Skopje',
    '42': 'RZH Ekonomika AD Skopje',
    '43': 'RZH Institut AD Skopje',
    '44': 'RZH Inter-Transshped AD Skopje',
    '79': 'RZH KPOR AD Skopje',
    '61': 'RZH Tehnichka kontrola AD Skopje',
    '45': 'RZH Uslugi AD Skopje',
    '71': 'Rudnici Banjani AD Skopje',
    '135': 'Sigurnosno staklo AD Prilep',
    '47': 'Sileks AD Kratovo',
    '46': 'Sileks Banka AD',
    '60': 'Skovin AD Skopje',
    '48': 'Skopski Pazar AD Skopje',
    '80': 'Slavej AD Skopje',
    '146': 'SN Osiguritelen Broker AD Bitola',
    '63': 'Sovremen dom AD Prilep',
    '27': 'Stater Banka AD',
    '75': 'Stokopromet AD Skopje',
    '49': 'Stopanska banka AD Bitola',
    '133': 'Stopanska banka AD Skopje',
    '119': 'Strumica tabak AD Strumica',
    '85': 'Strumichko pole AD s. Vasilevo',
    '87': 'Tajmishte AD Kichevo',
    '83': 'TEAL AD Tetovo',
    '50': 'Teteks AD Tetovo',
    '51': 'Teteks-Kreditna Banka AD',
    '52': 'Tetovska Banka AD',
    '74': 'Tehnokomerc AD Skopje',
    '53': 'Tehnometal Vardar AD Skopje',
    '54': 'Toplifikacija AD Skopje',
    '110': 'Transbalkan AD Gevgelija',
    '98': 'Trgotekstil maloprodazhba AD Skopje',
    '103': 'Triglav osiguruvanje AD Skopje',
    '100': 'Trikotazha Pelister AD Bitola',
    '105': 'Trudbenik AD Ohrid',
    '59': 'TTK Banka AD Skopje',
    '55': 'Tutunski kombinat AD Prilep',
    '62': 'Ugotur AD Skopje',
    '73': 'UNI Banka AD Skopje',
    '113': 'Fabrika Karposh AD Skopje',
    '56': 'Fakom AD Skopje',
    '57': 'Fershped AD Skopje',
    '130': 'FZC 11 Oktomvri AD Kumanovo',
    '65': 'Fruktal Mak AD Skopje',
    '78': 'Fustelarko Borec AD Bitola',
    '58': 'Hoteli-Metropol AD Ohrid',
    '120': 'Cementarnica USJE AD Skopje',
    '90': 'Centralna kooperativna banka AD Skopje'
}


def process_labels(dataset):
    processed_data = []

    for entry in dataset:
        if entry:
            score = entry[0]['score']

            rounded_score = round(score, 2)

            if rounded_score < 0.5:
                processed_data.append({'label': 'Negative', 'score': rounded_score})
            else:
                processed_data.append({'label': 'Positive', 'score': rounded_score})
        else:
            processed_data.append(None)

    return processed_data


if __name__ == '__main__':

    url = "https://api.seinet.com.mk/public/documents/single/"
    company_news = {}
    news_date = {}
    content_id = {}
    root = "src/main/resources/sent"

    LATEST_DOC_ID = 70721

    while True:
        adr = url + str(LATEST_DOC_ID + 1)
        response = requests.get(adr)
        if response.status_code.__str__().startswith("5"):
            break

        LATEST_DOC_ID += 1

    news_id = range(LATEST_DOC_ID, LATEST_DOC_ID - 700, -1)

    for i in news_id:
        adr = url + str(i)
        response = requests.get(adr)

        if response.status_code == 200:
            soup = BeautifulSoup(response.text, 'html.parser')
            try:
                json_data = json.loads(response.text)
                issuer_id = json_data['data']['issuerId']
                published_data = json_data['data']['publishedDate']

                if 'content' in json_data['data']:
                    content_id[i] = json_data['data']['content']
                else:
                    content_id[i] = None

                news_date[i] = published_data

                # if issuer_id not in company_news:
                #     company_news[issuer_id] = []
                # company_news[issuer_id].append(i)

                company_news[i] = issuer_id

            except json.JSONDecodeError:
                print("The response is not in JSON format. Index", i, file=sys.stderr)
        else:
            print(f"DOCUMENT RETREIVAL FAILURE | Status code: {response.status_code}", "PUBLIC INDEX OF DOCUMENT:",
                  i, file=sys.stderr)
            news_date[i] = None

    news_date_parsed = []

    for key in news_date.keys():
        if news_date[key] == None:
            date_index_map = {key: None}
            continue
        date = news_date[key].replace("T", " ").split(".")[0]
        index = key
        date_index_map = {key: date}
        news_date_parsed.append(date_index_map)

    file_path = f'{root}/news.csv'

    with open(file_path, mode='w', newline='') as file:
        writer = csv.writer(file)
        writer.writerow(["Index", "Date"])
        for entry in news_date_parsed:
            for key, value in entry.items():
                writer.writerow([key, value])

    news = pd.read_csv(f'{root}/news.csv')
    # news.head(10)
    content = []

    for key, val in zip(content_id.keys(), content_id.values()):
        if val is None:
            content.append(val)
            continue

        formatted = val.replace("<p>", "").replace("</p>", "").replace("&nbsp", " ").strip()
        content.append(formatted)

    # len(content)
    news['Content'] = content

    df = pd.DataFrame(news).copy()

    for idx, row in df.iterrows():
        if row['Content'] is None:
            continue
        if "automaticaly" in row['Content']:
            link = row['Content']
            referenced_index = None

            if '/document/' in link:
                referenced_index = link.split('/document/')[1].split('"')[0]

            if referenced_index:
                referenced_index = int(referenced_index)
                referenced_row = df[df['Index'] == referenced_index]
                if not referenced_row.empty:
                    df.at[idx, 'Content'] = referenced_row.iloc[0]['Content']

    companies = company_news.values()
    df['Company'] = list(map(lambda c: company_ids[str(c)], companies))
    df.to_csv(f'{root}/companies_news_analysis.csv')

    classifier = pipeline("sentiment-analysis", model='bert-base-multilingual-cased')
    company_ids = df['Company'].unique()

    filtered_dfs = []

    for id in company_ids:
        cpm = df[df['Company'] == id][:3]
        filtered_dfs.append(cpm)

    df = pd.concat(filtered_dfs)

    sentiment_analysis = []
    counter = 0

    for content in df['Content']:
        if content is None:
            sentiment_analysis.append(None)
            continue
        try:
            result = classifier(content)
            sentiment_analysis.append(result)
            counter += 1
        except Exception as e:
            print(f"Error processing content: {content}, Error: {e}", file=sys.stderr)
            sentiment_analysis.append(None)

    sentiment_analysis = process_labels(sentiment_analysis)

    analysis_list = []
    score_list = []
    for sentiment in sentiment_analysis:
        if sentiment is None:
            analysis_list.append(None)
            score_list.append(None)
            continue
        analysis = sentiment['label']
        score = sentiment['score']

        analysis_list.append(analysis)
        score_list.append(round(score, 3))

    df['Sentiment analysis'] = analysis_list
    df['Score'] = score_list
    df.to_csv(f'{root}/sentiment_analysis.csv')

    exit(0)