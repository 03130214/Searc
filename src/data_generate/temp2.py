import random

def randishes(location):
    # 定义种类数组
    categories = ["小吃", "川菜", "徽菜", "粤菜", "闽菜", "湘菜", "苏菜", "鲁菜", "浙菜"]

    # 定义不同种类的食物数组
    snacks = ["包子", "羊杂面", "盖浇饭", "石锅豆腐", "拌面", "炸酱面", "牛肉汤", "清河肉饼"]
    sichuan_dishes = ["麻婆豆腐", "回锅肉", "宫保鸡丁", "鱼香肉丝", "红烧牛肉", "水煮肉片", "肉末茄子", "口水鸡", "香辣虾", "辣子鸡"]
    huizhou_dishes = ["徽州臭鳜鱼", "毛豆腐", "火腿炖甲鱼"]
    yue_dishes = ["白切鸡", "烧鹅", "红烧乳鸽", "脆皮烧肉", "煲仔饭", "文昌鸡", "白灼虾", "荷叶包饭", "虾饺", "糯米鸡"]
    min_dishes = ["海蛎煎", "太极芋泥", "白炒鲜竹蛏", "南煎肝", "荔枝肉", "醉排骨", "肉燕", "兴化米粉", "红糟鱼", "五柳居"]
    xiang_dishes = ["辣椒炒肉", "剁椒鱼头", "湘西外婆菜", "牛肉粉", "衡阳鱼粉", "宁远酿豆腐", "东安鸡", "姊妹团子", "栖凤渡鱼粉", "组庵豆腐"]
    su_dishes = ["金陵烤鸭", "彭城鱼丸", "老鸭汤", "清炖蟹粉狮子头", "金香饼", "红烧沙光鱼", "凤尾虾", "无锡肉骨头", "溜子鸡", "糖醋鳜鱼"]
    lu_dishes = ["一品豆腐", "糖醋鲤鱼", "九转大肠", "油焖大虾", "醋椒鱼", "木须肉", "糖醋里脊", "把子肉", "乌鱼蛋汤", "香酥鸡"]
    zhe_dishes = ["西湖醋鱼", "东坡肉", "赛蟹羹", "荷叶粉蒸肉", "龙井虾仁", "干菜焖肉", "叫化童鸡", "冰糖甲鱼", "蜜汁灌藕", "宁波汤团"]

    for _ in range(15):  # 生成15条记录
        category = random.choice(categories)
        if category == "小吃":
            food = random.choice(snacks)
        elif category == "川菜":
            food = random.choice(sichuan_dishes)
        elif category == "徽菜":
            food = random.choice(huizhou_dishes)
        elif category == "粤菜":
            food = random.choice(yue_dishes)
        elif category == "闽菜":
            food = random.choice(min_dishes)
        elif category == "湘菜":
            food = random.choice(xiang_dishes)
        elif category == "苏菜":
            food = random.choice(su_dishes)
        elif category == "鲁菜":
            food = random.choice(lu_dishes)
        elif category == "浙菜":
            food = random.choice(zhe_dishes)
        else:
            food = '未知食物'  # 如果没有匹配的种类，则默认为未知食物

        # 生成随机评分，范围在8.5到9.9之间，保留一位小数
        rating = round(random.uniform(8.5, 9.9), 1)

        # 生成85到99之间的随机整数
        integer = random.randint(85, 99)

        # 生成最终字符串
        result = f"('{food}', '{category}', {rating}, {integer}, '{location}');"

        # 输出结果
        print("INSERT INTO dishes (name, kind, score, hot, location) \n"
              f"VALUES {result}")

def main():
    locations = []
    print("请输入地点（输入 'exit' 结束输入）：")
    while True:
        location = input()
        if location.lower() == 'exit':
            break
        locations.append(location)

    for location in locations:
        randishes(location)

if __name__ == "__main__":
    main()
