//Bartosz Bugajski - 5
import java.util.Scanner;

class Car // wagon
{
    // lista podwojna, wiec mamy zarowno prev jak i next
    public Car prev; // poprzedni wagon
    public String name; // nazwa wagonu
    public Car next; // nastepny wagon
    public Car(String w)
    {
        name = w;
        prev = null;
        next = null;
    }
}

class Train // pociag (oraz lista wagonow)
{
    // lista pojedyncza, wiec mamy tylko next
    public String name; // nazwa pociagu
    public Train next; // nastepny pociag

    // informacje o pierwszym i ostatnim wagonie pociagu
    public Car first; // pierwszy wagon pociagu
    public Car last; // ostatni wagon pociagu
    public Train(String t, String w)
    {
        name = t;
        first = new Car(w); // kazdy nowy pociag ma od razu jeden wagon
        next = null;
        last = first;
    }
}

class TrainList // lista pociagow
{
    public Train first; // pierwszy pociag na liscie
    public TrainList()
    {
        first = null;
    }
    public Train Locate(String t) // metoda znajdujaca pociag o podanej nazwie
    {
        Train tr = first;
        while(!tr.name.equals(t))
        {
            tr = tr.next;
        }
        return tr;
    }
    public void New(String t1, String w) // metoda tworzaca nowy pociag o nazwie t1 z wagonem o nazwie w
    {
        Train tr = new Train(t1, w);

        // pociag dodajemy na poczatek listy, wiec ustawiamy next na pierwszy pociag na liscie, a nastepnie zmieniamy
        // first na dodawany pociag
        tr.next = first;
        first = tr;
    }
    public void Delete(Train tr, Train trprev) // metoda usuwajaca pociag tr z listy
    {
        if(first == tr)
        {
            // jesli usuwany pociag jest pierwszy na liscie to tylko zmieniamy first na nastepny
            first = tr.next;
        }
        else
        {
            // w innym przypadku zmieniamy next poprzedzajacego pociagu na next pociagu usuwanego
            trprev.next = tr.next;
        }
    }
}

public class Source
{
    static Scanner sc = new Scanner(System.in);
    public static void main(String[] args)
    {
        int z = sc.nextInt(); // wczytanie liczby zestawow
        for(int i = 0; i < z; i++)
        {
            TrainList list = new TrainList(); // tworzymy nowa liste
            int n = sc.nextInt(); // wczytanie ilosci polecen
            for(int j = 0; j < n; j++)
            {
                String instruction = sc.next(); // wczytanie polecenia
                if(instruction.equals("New"))
                {
                    String t1 = sc.next(); // wczytanie nazwy dodawanego pociagu
                    String w = sc.next(); // wczytanie nazwy wagonu dodawanego pociagu
                    list.New(t1, w);
                }
                else if(instruction.equals("InsertFirst"))
                {
                    String t1 = sc.next();
                    String w = sc.next();
                    Train tr = list.Locate(t1); // znalezienie pociagu o podanej nzawie
                    Car cr = new Car(w); // stworzenie nowego wagonu
                    cr.next = tr.first; // wstawiamy na poczatek wiec nastepny nowego wagonu = pierwszy
                    tr.first.prev = cr; // poprzedni pierwszego = nowy wagon
                    tr.first = cr; // zmiana pierwszego na dodany
                }
                else if(instruction.equals("InsertLast"))
                {
                    String t1 = sc.next();
                    String w = sc.next();
                    Train tr = list.Locate(t1);
                    Car cr = new Car(w);
                    cr.prev = tr.last; // wstawiamy na koniec wiec poprzedni nowego wagonu = ostatni
                    tr.last.next = cr; // nastepny ostatniego = nowy
                    tr.last = cr; // zmiana ostatniego na dodany
                }
                else if(instruction.equals("Display"))
                {
                    String t1 = sc.next();
                    Train tr = list.Locate(t1);
                    Car cr = tr.first;
                    StringBuilder sb = new StringBuilder(); // output
                    sb.append(t1);
                    sb.append(": ");
                    boolean rev = false; // zmienna przechowująca informacje o tym czy pociag na danym odcinku jest odwrocony
                    Car prev = cr; // zmienna przechowujaca informacje o poprzednim wagonie
                    while(cr != null) // przechodzimy przez wszystkie wagony
                    {
                        sb.append(cr.name); // dodajemy nazwe do zmiennej ktora wypiszemy na wyjscie
                        sb.append(' '); // dodajemy znak spacji

                        // jesli aktualny odcinek nie jest odwrocony to sprawdzamy czy on aktualnie sprawdzanego wagonu
                        // zaczyna sie odcinek odwrocony, w tym celu sprawdzamy czy nastepny wagon jest rowny poprzednio
                        // sprawdzanemu wagonowi
                        if(!rev && cr.next == prev)
                        {
                            rev = true;
                        }
                        // jesli aktualny odcinek jest odwrocony to musimy sprawdzic czy nie jest to koniec odwroconego
                        // odcinka, w tym celu sprawdzamy czy poprzednio sprawdzany wagon jest rowny poprzedniemu wagonowi
                        if(rev && cr.prev == prev)
                        {
                            rev = false;
                        }
                        prev = cr;
                        if(!rev) // jesli nie jest odwrocony to przechodzimy po nextach
                        {
                            cr = cr.next;
                        }
                        else // jesli jest odwrocony to przechodzimy po prevach
                        {
                            cr = cr.prev;
                        }
                    }
                    System.out.println(sb); // wypisujemy zmienna wyjsciowa
                }
                else if(instruction.equals("TrainsList"))
                {
                    Train tr = list.first;
                    StringBuilder sb = new StringBuilder();
                    sb.append("Trains: ");
                    while(tr != null) // przechodzimy przez liste od poczatku do konca
                    {
                        sb.append(tr.name); // dodajemy nazwe do zmiennej wyjsciowej
                        sb.append(' '); // dodajemy spacje
                        tr = tr.next; // przechodzimy do nastepnego pociagu
                    }
                    System.out.println(sb); // wypisujemy zmienna wyjsciowa
                }
                else if(instruction.equals("Reverse"))
                {
                    String t1 = sc.next();
                    Train tr = list.first;
                    while(!tr.name.equals(t1)) // szukamy pociagu o danej nazwie
                    {
                        tr = tr.next;
                    }
                    // aby zrobic reverse w czasie o(1), mozemy po prostu zamienic miejscami pierwszy wagon z ostatnim
                    // a nastepnie zamienic w nich prev z nextem
                    // cala lista nie jest odwracana, gdyz nie jest to mozliwe do zrobienia w czasie o(1), reszta
                    // reverse'a dzieje sie w display'u
                    Car pom = tr.first;
                    tr.first = tr.last;
                    tr.first.next = tr.first.prev;
                    tr.first.prev = null;
                    tr.last = pom;
                    tr.last.prev = tr.last.next;
                    tr.last.next = null;
                }
                else if(instruction.equals("Union"))
                {
                    String t1 = sc.next();
                    String t2 = sc.next();
                    Train tr1 = list.first;
                    Train tr2 = list.first;
                    Train trprev = null; // zmienna potrzebna do wywolania delete w czasie o(1), jest to pociag
                    // poprzedzajacy tr2 na liscie

                    // szukamy jednoczesnie obu pociagow dla zwiekszenia efektywnosci
                    while(!tr2.name.equals(t2) || !tr1.name.equals(t1))
                    {
                        if(tr2.next.name.equals(t2)) // jesli nazwa nastepnego jest rowna szukanej t2, to znalezlismy trprev
                        {
                            trprev = tr2;
                        }
                        if(!tr2.name.equals(t2))
                        {
                            tr2 = tr2.next;
                        }
                        if(!tr1.name.equals(t1))
                        {
                            tr1 = tr1.next;
                        }
                    }
                    tr1.last.next = tr2.first; // zmieniamy next ostatniego wagonu pierwszego pociagu na pierwszy wagon drugiego
                    tr2.first.prev = tr1.last; // zmieniamy prev pierwszego wagonu drugiego pociagu na ostatni wagon pierwszego
                    tr1.last = tr2.last; // zmieniamy last pierwszego pociagu na last drugiego pociagu
                    list.Delete(tr2, trprev); // usuwamy drugi pociag z listy
                }
                else if(instruction.equals("DelFirst"))
                {
                    String t1 = sc.next();
                    String t2 = sc.next();
                    Train tr = list.first;
                    Train trprev = null; // potrzebujemy informacji o prev pierwszego pociagu, bo jest
                    // mozliwosc ze bedziemy musieli usunac ten pociag z listy
                    while(!tr.name.equals(t1)) // szukamy pociagu o nazwie t1
                    {
                        if(tr.next.name.equals(t1))
                        {
                            trprev = tr;
                        }
                        tr = tr.next;
                    }
                    // jesli last==first to znaczy ze w pierwszym pociagu byl tylko jeden wagon, wiec musimy usunac pociag z listy
                    if(tr.first == tr.last)
                    {
                        list.Delete(tr, trprev); // usuwamy tr z listy
                        list.New(t2, tr.first.name); // dodajemy tr2 do listy
                    }
                    else
                    {
                        list.New(t2, tr.first.name); // dodajemy tr2 do listy
                        if(tr.first.next.next == tr.first) // sprawdzamy czy pociag jest odwrocony na poczatku
                        {
                            tr.first = tr.first.next;
                            tr.first.next = tr.first.prev;
                            tr.first.prev = null;
                        }
                        else
                        {
                            tr.first = tr.first.next;
                            tr.first.prev = null;
                        }
                    }
                }
                else if(instruction.equals("DelLast")) // działa bardzo podobnie do DelFirst
                {
                    String t1 = sc.next();
                    String t2 = sc.next();
                    Train tr = list.first;
                    Train trprev = null;
                    while(!tr.name.equals(t1))
                    {
                        if(tr.next.name.equals(t1))
                        {
                            trprev = tr;
                        }
                        tr = tr.next;
                    }
                    if(tr.first == tr.last)
                    {
                        list.Delete(tr, trprev);
                        list.New(t2, tr.last.name);
                    }
                    else
                    {
                        list.New(t2, tr.last.name); // tworzymy nowy pociag o nazwie t2
                        if(tr.last.prev.prev == tr.last) // sprawdzamy czy pierwszy pociag jest odwrocony na koncu
                        {
                            tr.last = tr.last.prev;
                            tr.last.prev = tr.last.next;
                            tr.last.next = null;
                        }
                        else
                        {
                            tr.last = tr.last.prev;
                            tr.last.next = null;
                        }
                    }
                }
            }
        }
    }
}

/*
1
36
New T1 W1
InsertFirst T1 W2
InsertFirst T1 W3
InsertFirst T1 W4
InsertLast T1 W5
Display T1
Reverse T1
Display T1
DelFirst T1 T2
TrainsList
Display T1
DelLast T2 T3
TrainsList
Union T3 T1
Display T3
Reverse T3
InsertLast T3 V1
InsertLast T3 V2
InsertLast T3 V3
InsertLast T3 V4
InsertFirst T3 V5
InsertFirst T3 V6
InsertFirst T3 V7
InsertFirst T3 V8
Display T3
Reverse T3
DelFirst T3 T1
Display T3
DelLast T3 T2
TrainsList
Display T3
Union T1 T2
Reverse T1
Union T1 T3
Reverse T1
Display T1

T1: W4 W3 W2 W1 W5
T1: W5 W1 W2 W3 W4
Trains: T2 T1
T1: W1 W2 W3 W4
Trains: T3 T1
T3: W5 W1 W2 W3 W4
T3: V8 V7 V6 V5 W4 W3 W2 W1 W5 V1 V2 V3 V4
T3: V3 V2 V1 W5 W1 W2 W3 W4 V5 V6 V7 V8
Trains: T2 T1 T3
T3: V3 V2 V1 W5 W1 W2 W3 W4 V5 V6 V7
T1: V7 V6 V5 W4 W3 W2 W1 W5 V1 V2 V3 V4 V8
 */