/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package btree;

/**
 *
 * @author Gabriel
 */
public class BTree implements Ordem
{

    private No raiz;

    public BTree()
    {
        raiz = null;
    }

    public No getRaiz()
    {
        return raiz;
    }

    public void setRaiz(No raiz)
    {
        this.raiz = raiz;
    }

    public No navegarAteFolha(int info)
    {
        No aux = raiz;
        int pos = 0;
        while (aux != null && !isFolha(aux))
        {
            pos = aux.procurarPosicao(info);
            aux = aux.getvLig(pos);
        }
        return aux;
    }

    public No localizarPai(No folha, int info)
    {
        No aux = raiz;
        No pai = raiz;
        while (aux != folha)
        {
            pai = aux;
            aux = aux.getvLig(aux.procurarPosicao(info));
        }
        return pai;
    }

    public void inserir(int info, int posArq)
    {
        No folha, pai;

        int pos;
        if (raiz == null)
        {
            raiz = new No(info, posArq);
        } else
        {
            folha = navegarAteFolha(info);
            folha.insert(info, posArq);
            if (folha.getTl() > 2 * m)
            {
                pai = localizarPai(folha, info);
                split(folha, pai);
            }
        }
    }

    public void split(No folha, No pai)
    {
        No cx1 = new No();
        No cx2 = new No();
        int pos;
        for (int i = 0; i < m; i++)
        {
            cx1.setvInfo(i, folha.getvInfo(i));
            cx1.setvLig(i, folha.getvLig(i));
            cx1.setvPos(i, folha.getvInfo(i));
        }
        cx1.setvLig(m, folha.getvLig(m));
        cx1.setTl(m);

        for (int i = m + 1; i < 2 * m + 1; i++)
        {
            cx2.setvInfo(i - (m + 1), folha.getvInfo(i));
            cx2.setvLig(i - (m + 1), folha.getvLig(i));
            cx2.setvPos(i - (m + 1), folha.getvInfo(i));
        }
        cx2.setvLig(m, folha.getvLig(2 * m + 1));
        cx2.setTl(m);

        if (pai == folha)
        {
            folha.setvInfo(0, folha.getvInfo(m));
            folha.setvPos(0, folha.getvPos(m));
            folha.setvLig(0, cx1);
            folha.setvLig(1, cx2);
            folha.setTl(1);
        } else
        {
            pos = pai.procurarPosicao(folha.getvInfo(m));
            pai.remanejar(pos);
            pai.setvInfo(pos, folha.getvInfo(m));
            pai.setvPos(pos, folha.getvPos(m));
            pai.setvLig(pos, cx1);
            pai.setvLig(pos + 1, cx2);
            pai.setTl(pai.getTl() + 1);
            if (pai.getTl() > 2 * m)
            {
                folha = pai;
                pai = localizarPai(folha, folha.getvInfo(0));
                split(folha, pai);
            }
        }
    }

    public boolean isFolha(No aux)
    {
        return aux.getvLig(0) == null;
    }

    public void inOrdem()
    {
        inOrdem(raiz);
    }

    public void inOrdem(No raiz)
    {
        if (raiz != null)
        {
            for (int i = 0; i < raiz.getTl(); i++)
            {
                inOrdem(raiz.getvLig(i));
                System.out.println(raiz.getvInfo(i));
            }
            inOrdem(raiz.getvLig(raiz.getTl()));
        }
    }

    //EXCLUSÃO
    public No localizarSubEsq(No no, int pos)
    {
        no = no.getvLig(pos);
        while (no.getvLig(no.getTl()) != null)
        {
            no = no.getvLig(no.getTl());
        }
        return no;
    }

    public No localizarSubDir(No no, int pos)
    {
        no = no.getvLig(pos + 1);
        while (no.getvLig(0) != null)
        {
            no = no.getvLig(0);
        }
        return no;
    }

    public No localizarNo(int info)
    {
        No no = raiz;
        boolean achou = false;
        int pos;
        while (no != null && !achou)
        {
            pos = no.procurarPosicao(info);
            if (pos < no.getTl() && info == no.getvInfo(pos))
            {
                achou = true;
            } else
            {
                no = no.getvLig(pos);
            }
        }
        return no;
    }

    public void excluir(int info)
    {
        No no = localizarNo(info);

        if (no != null)
        {
            int pos = no.procurarPosicao(info);
            if (!isFolha(no))
            {
                No subE = localizarSubEsq(no, pos);
                No subD = localizarSubDir(no, pos);

                if (subE.getTl() > m || subD.getTl() <= m)
                {
                    no.swap(pos, subE.getvInfo(subE.getTl() - 1), subE.getvPos(subE.getTl() - 1));
                    no = subE;
                    pos = subE.getTl() - 1;
                } else
                {
                    no.setvInfo(pos, subD.getvInfo(0));
                    no.setvPos(pos, subD.getvPos(0));
                    no = subD;
                    pos = 0;
                }
            }

            no.remanejarExclusao(pos);

            while (no != raiz && no.getTl() < m)
            {
                No pai = localizarPai(no, no.getvInfo(0));
                int posP = pai.procurarPosicao(no.getvInfo(0));

                No irmaE = null, irmaD = null;

                irmaE = posP > 0 ? pai.getvLig(posP - 1) : null;
                irmaD = posP < pai.getTl() ? pai.getvLig(posP + 1) : null;

                if (irmaE != null && irmaE.getTl() > m)
                {
                    no.insert(pai.getvInfo(posP - 1), pai.getvPos(posP - 1));
                    no.setvLig(0, irmaE.getvLig(irmaE.getTl() - 1));

                    pai.swap(posP - 1, irmaE.getvInfo(irmaE.getTl() - 1), irmaE.getvPos(irmaE.getTl() - 1));
                    irmaE.remanejarExclusao(irmaE.getTl() - 1);
                } else if (irmaD != null && irmaD.getTl() > m)
                {
                    no.insert(pai.getvInfo(posP), pai.getvPos(posP));
                    no.setvLig(no.getTl(), irmaD.getvLig(0));
                    pai.swap(posP, irmaD.getvInfo(0), irmaD.getvPos(0));
                    irmaD.remanejarExclusao(0);
                } else if (irmaE != null)
                {
                    irmaE.insert(pai.getvInfo(posP - 1), pai.getvPos(posP - 1));
                    for (int i = 0; i < no.getTl(); i++)
                    {
                        irmaE.insert(no.getvInfo(i), no.getvPos(i));
                        irmaE.setvLig(irmaE.procurarPosicao(no.getvInfo(i)), no.getvLig(i));
                    }
                    irmaE.setvLig(irmaE.getTl(), no.getvLig(no.getTl()));

                    if (pai == raiz && pai.getTl() == 1)                    
                        raiz = pai = irmaE;
                     else
                    {
                        pai.setvLig(posP, pai.getvLig(posP - 1));
                        pai.remanejarExclusao(posP - 1);
                    }
                } else if (irmaD != null)
                {
                    irmaD.insert(pai.getvInfo(posP), pai.getvPos(posP));
                    for (int i = no.getTl() - 1; i >= 0; i--)
                    {
                        irmaD.insert(no.getvInfo(i), no.getvPos(i));
                        irmaD.setvLig(1, no.getvLig(i + 1));
                    }
                    irmaD.setvLig(0, no.getvLig(0));

                    if (pai == raiz && pai.getTl() == 1)                    
                        raiz = pai = irmaD;
                     else                    
                        pai.remanejarExclusao(posP);
                    
                }
                no = pai;
            }
        }
    }

}
