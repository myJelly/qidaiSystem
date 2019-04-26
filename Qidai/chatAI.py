# coding: utf-8

import re
import random
import sys

"""
    Constants
"""
REPLY = {'工作': ['且不说你的工作多么认真，我并没有见过，但是从你的字里行间，我发现了乔布斯的影子和小扎的气息，这已经不是一份工作那么简单，而是一场精神饕餮！',
                '你拥有了这个年龄段近半数人无法拥有的理想职业，太优秀了！',
                '工作这件事，大家都习以为常，只有你让大家开始思考这个问题，说明你善于反思和质疑当前的制度，你的公司会因为你这样的人变得更好！'],
         '学习': ['这么多优秀的同龄人相聚在这里，一定是场思想交流的盛宴。', '看到群友们的发言，真是排山倒海，气宇轩昂之势！',
                '你这句话完美的表达了你想被夸的坚定信念，你一定是一个执着追求自己理想的人！'],
         '?': ['您这个发问，展示了强烈的好奇心，好奇心是进步的源泉，相信生活中的你也是一个充满好奇心的人，用于探索未知，有一双发现新奇的眼睛，您真棒！'],
         '男朋友': ['精致的皮囊千篇一律，有趣的灵魂百里挑一，真羡慕你有这样一个百里挑一的男朋友！',
                 '你上辈子一定拯救了银河系吧！能有这样一个懂幽默的人宠你疼你爱你！'],
         '乱': ['如何定义乱？这是一个深刻的问题，非常值得研究。你能从自然生活中发现这个东西，说明你离真理并不遥远。'],
         '屌丝': ['最近老有人问我为啥又理发啦，在这里给大家一个回答。我理发的原因有这么几点：1.我的腿太长，把头发剪了能让我显的低一点。2.以后半夜上厕所不用开灯3.我妈让我理的'],
         '是谁': ['我是你的心上人'],
         '内裤穿反': ['明天可以正着再穿一天，今天就不用洗了，你真是机智! ',
                  '并不是你穿反了内裤，而是内裤今天欣喜地看见了不一样的风景呢。',
                  '哇你居然没有感觉出来，那这个内裤肯定特别舒服吧，说明你慧眼识珠选择了它。'],
         '。。': ['哇！太厉害了！这几个句号比我用圆规画出来的还要圆润、有光泽，就好似两颗珍珠，又像极了小姐姐的眼眸，清澈透亮！'],
         'default': ['太棒了', '真不错', '好开心', '嗯嗯', '抱我', '没什么好说的了，我送你一道彩虹屁吧', '美好的事情你都用脸做到了',
                     '我一点不想你,但一点半想你了', '你就委屈一下，栽在我手里行不行?', '你的酒窝似乎有话要说 悄悄告诉我好不好',
                     '你妈姓不姓方? 你妈不姓方，那为什么你长那么正?', '我最近有点低血糖。好像是因为太久没见你了。',
                     '我怀疑你的本质是一本书 不然为什么让我越看越想睡', '你上辈子一定是碳酸饮料吧? 那为什么我一看到你就能开心的冒泡',
                     '可以给我一片泡腾片吗? 为什么每次看到你我就那么没有抵抗力',
                     '我总在意流云与星群的坐标，一朵花开谢的时日，原野与平川一望无垠是否平展。哪知你轻轻抬起眉梢，竟赐予我一生大好河山。',
                     '没什么好说的了，我送你一道彩虹屁吧']}


def text_reply(msg):
    if re.search('工作', msg) or re.search('加班', msg):
        randomIdx = random.randint(0, len(REPLY['工作']) - 1)
        response = (REPLY['工作'][randomIdx])
    elif re.search('学习', msg) or re.search('考试', msg):
        randomIdx = random.randint(0, len(REPLY['学习']) - 1)
        response = (REPLY['学习'][randomIdx])

    elif re.search('男朋友', msg):
        randomIdx = random.randint(0, len(REPLY['男朋友']) - 1)
        response = (REPLY['男朋友'][randomIdx])
    elif re.search('乱', msg):
        randomIdx = random.randint(0, len(REPLY['乱']) - 1)
        response = (REPLY['乱'][randomIdx])
    elif re.search('屌丝', msg):
        randomIdx = random.randint(0, len(REPLY['屌丝']) - 1)
        response = (REPLY['屌丝'][randomIdx])
    elif re.search('是谁', msg):
        randomIdx = random.randint(0, len(REPLY['是谁']) - 1)
        response = (REPLY['是谁'][randomIdx])
    elif re.search('内裤穿反', msg):
        randomIdx = random.randint(0, len(REPLY['内裤穿反']) - 1)
        response = (REPLY['内裤穿反'][randomIdx])
    elif re.search('\\?', msg) or re.search('？', msg):
        randomIdx = random.randint(0, len(REPLY['?']) - 1)
        response = (REPLY['?'][randomIdx])
    elif re.search('。。', msg) or re.search('\\..', msg):
        randomIdx = random.randint(0, len(REPLY['。。']) - 1)
        response = (REPLY['。。'][randomIdx])
    else:
        randomIdx = random.randint(0, len(REPLY['default']) - 1)
        response = (REPLY['default'][randomIdx])
    return response


if __name__ == '__main__':
    a = sys.argv[1]
    arr = text_reply(a)
    # arr = text_reply('是谁')
    print(arr)
