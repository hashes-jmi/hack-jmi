package com.expledia.HealthPlus;

/**
 * Created by shubhamg931 on 24/3/18.
 */

public class remediesFragmentJ extends remediesFragment {

//
//    @NotNull
//    @Override
//    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//
//        View myView = new View(getContext());
//        myView=LayoutInflater.from(getContext()).inflate(R.layout.remedies_fragment, container, false);
////        final ArrayList<boolean[]> selectedSymptoms = new ArrayList<>();
//        final ArrayList<Boolean> myboolean=new ArrayList<Boolean>();
//        final ArrayList<String> ans = new ArrayList<>();
//        final ArrayList<String> options = new ArrayList<>();
//        options.add("1");
//        options.add("2");
//        options.add("3");
//        options.add("A");
//        options.add("B");
//        options.add("C");
//
//        MultiSelectSpinner multiSelectSpinner1 = (MultiSelectSpinner) myView.findViewById(R.id.multiselectSpinner);
//        multiSelectSpinner1.setItems(options)
//
//                .setListener(new MultiSelectSpinner.MultiSpinnerListener() {
//                    @Override
//                    public void onItemsSelected(boolean[] selected) {
//                        myboolean.clear();
//                        for (int i=0;i<selected.length;++i){
//                            myboolean.add(selected[i]);
//                            if(selected[i] == true)
//                                ans.add(options.get(i));
//
//                        }
//
//                    }
//                })
//                .setAllCheckedText("All types")
//                .setAllUncheckedText("none selected")
//                .setSelectAll(false);
//        ;
//
//
//        ListView list = (ListView) myView.findViewById(R.id.remedyList);
//        ArrayAdapter adapter = new ArrayAdapter<String>(getContext());
//        return myView;
//    }
}
