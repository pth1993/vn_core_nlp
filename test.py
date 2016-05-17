from vn_core_nlp.preprocessing import sentence_detector, word_tokenizer, pos_tagger


sd = sentence_detector.SentenceDetector()
sd.sentence_detector('/home/hoang/Desktop/content.txt', '/home/hoang/Desktop/content_new.txt')

#tokenizer = word_tokenizer.WordTokenizer()
#tokenizer.tokenize_file('/home/hoang/Desktop/content_new.txt', '/home/hoang/Desktop/content_tok.txt')

#postagger = pos_tagger.POSTagger()
#postagger.pos_tagger('/home/hoang/Desktop/content_tok.txt', '/home/hoang/Desktop/content_pos.txt')
