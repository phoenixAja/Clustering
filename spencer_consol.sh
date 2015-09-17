# 1) Blocking
#java -cp SpencerConsolidated-BlockingStep/out/production/SpencerConsolidated-BlockingStep:./javalibs/lucene-core-4.10.3.jar Driver "data/sampledata.fps" 1000 0.5 | tee test/cand_set

# 2) Similarity computation
#java -cp SpencerConsolidated-SimilarityStep/out/production/SpencerConsolidated-SimilarityStep:./javalibs/lucene-core-4.10.3.jar Driver "data/sampledata.fps" "test/cand_set" "test/sim_values.txt"

# 3a) Clustering and connected components computation (optional)
#java -cp SpencerConsolidated-ConnectedComponentsStep/out/production/SpencerConsolidated-ConnectedComponentsStep:./javalibs/trove-3.1a1.jar DriverClusterCreation  "test/sim_values.txt" "test/cc_values.txt"

#rm -rf test/cc_out_dir/
mkdir -p  test/cc_out_dir/
3#java -cp SpencerConsolidated-ConnectedComponentsStep/out/production/SpencerConsolidated-ConnectedComponentsStep:./javalibs/trove-3.1a1.jar DriverConnectedComponents "test/cc_values.txt" "test/cc_out_dir/"

# 3b) Connected components computation
#rm -rf test/sim_out_dir/
#mkdir -p  test/sim_out_dir/
#java -cp SpencerConsolidated-ConnectedComponentsStep/out/production/SpencerConsolidated-ConnectedComponentsStep:./javalibs/trove-3.1a1.jar DriverConnectedComponents "test/sim_values.txt" "test/sim_out_dir/"

# 4) Cluster output
java -cp SpencerConsolidated-ClusterOutputStep/out/production/SpencerConsolidated-ClusterOutputStep Driver "data/sampledata.fps" "test/sim_out_dir/" 31 "test/outfile_cz_gt_1.txt" "test/outfile_cz_eq_1.txt"
