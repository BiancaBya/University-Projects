"""
lista = [1,2,3,4,5]
print(lista[-1])    # ultimul element

n = 5
for i in range(n):    # de la 0 pana la n-1, <=> for i in range(0, n) pt ca n se ia exclusiv
    print(i)
"""


def f2(n):
    if n<=0: raise ValueError()
    l = [x for x in range(n-1,-1,-1)]
    for i in range(n-1):
        l[i+1] += l[i]
    return l[-1]

def test():

    n = -3
    try:
        f2(n)
        assert False
    except ValueError:
        assert True

    n = 0
    try:
        f2(n)
        assert False
    except ValueError:
        assert True

    assert f2(6) == 15


test()







