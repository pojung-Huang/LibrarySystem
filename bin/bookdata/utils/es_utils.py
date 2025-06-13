from elasticsearch import Elasticsearch

def save_to_es(df):
    es = Elasticsearch("http://localhost:9200")

    index_name = "libraries"
    if es.indices.exists(index=index_name):
        es.indices.delete(index=index_name)
    es.indices.create(index=index_name)

    for i, row in df.iterrows():
        doc = row.to_dict()
        es.index(index=index_name, id=i, document=doc)

    print("✅ 資料已匯入 Elasticsearch")
