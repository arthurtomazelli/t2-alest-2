import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Map;
import java.util.HashMap;

public class IndexMinHeap<Key, Value extends Comparable<Value>> {
  private class ValuePos {
    public ValuePos(Value v, int p) { this.value = v; this.pos =p; }
    public Value value;
    public int pos;
  }

  private Key[] pq; 
  private Map<Key, ValuePos> dic;
  private int n; 

  public IndexMinHeap(int initCapacity) {
    pq = (Key[]) new Object[initCapacity + 1];
    dic = new HashMap<>();
    n = 0;
  }

  public IndexMinHeap() {
    this(1);
  }

  public IndexMinHeap(Key[] keys, Value[] values) {
    n = keys.length;
    pq = (Key[]) new Object[keys.length + 1];
    dic = new HashMap<>();
    for (int i = 0; i < n; i++) {
      pq[i + 1] = keys[i];
      dic.put(keys[i], new ValuePos(values[i],i+1));
    }
    for (int k = n / 2; k >= 1; k--)
      sink(k);
  }

  public boolean isEmpty() {
    return n == 0;
  }

  public int size() {
    return n;
  }

  public Key min() {
    if (isEmpty())
      throw new NoSuchElementException("Heap vazio!");
    return pq[1];
  }

  private void resize(int capacity) {
    assert capacity > n;
    Key[] temp = (Key[]) new Object[capacity];
    Map<Key, ValuePos> newdic = new HashMap<>();
    for (int i = 1; i <= n; i++) {
      temp[i] = pq[i];
      newdic.put(pq[i], dic.get(pq[i]));
    }
    pq = temp;
    dic = newdic;
  }

  public void insert(Key x, Value v) {
    if (n == pq.length - 1)
      resize(2 * pq.length);

    pq[++n] = x;
    dic.put(x,new ValuePos(v, n));
    swim(n);
  }

  public Key delMin() {
    if (isEmpty())
      throw new NoSuchElementException("Heap vazio!");
    Key min = pq[1];
    exch(1, n--);
    sink(1);
    pq[n + 1] = null; 
    dic.remove(min);
    if ((n > 0) && (n == (pq.length - 1) / 4))
      resize(pq.length / 2);
    return min;
  }

  public boolean contains(Key k) {
    return dic.containsKey(k);
  }

  public void decreaseValue(Key k, Value v) {
    if (!contains(k)) throw new NoSuchElementException("Chave não existe");
    ValuePos vp = dic.get(k);
    vp.value = v;
    swim(vp.pos);
  }

  private void swim(int k) {
    while (k > 1 && greater(k / 2, k)) {
      exch(k, k / 2);
      k = k / 2;
    }
  }

  private void sink(int k) {
    while (2 * k <= n) {
      int j = 2 * k;
      if (j < n && greater(j, j + 1))
        j++;
      if (!greater(k, j))
        break;
      exch(k, j);
      k = j;
    }
  }

  private boolean greater(int i, int j) {
    ValuePos vp1 = dic.get(pq[i]);
    ValuePos vp2 = dic.get(pq[j]);
    return vp1.value.compareTo(vp2.value) >  0;
  }

  private void exch(int i, int j) {
    ValuePos vp1 = dic.get(pq[i]);
    ValuePos vp2 = dic.get(pq[j]);
    Key swap = pq[i];
    pq[i] = pq[j];
    pq[j] = swap;
    vp1.pos = j;
    vp2.pos = i;
  }
}